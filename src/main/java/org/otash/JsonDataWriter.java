package org.otash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.RecursiveAction;


/**
 * Used to create and insert mock json objects by using {@link java.util.concurrent.ForkJoinPool} frameWork
 */
public class JsonDataWriter extends RecursiveAction {


    /**
     * Represents a user request to generate a mock data
     */
    private final FakerApplicationGenerateRequest request;

    /**
     * Determines when it's time to fork the task into subtasks. If the tasks number exceeds the
     * THRESHOLD, then it forks. Explained in {@link JsonDataWriter#compute()}
     */
    private static final int THRESHOLD = 10000;

    /**
     * Represents how many rows of json data this object should generate.
     */
    private final int tasksNumber;

    /**
     * Represents to what file all generated date should be inserted
     */
    private final String PATH;

    /**
     * @param request     {@link org.otash.JsonDataWriter#request}
     * @param tasksNumber {@link org.otash.JsonDataWriter#tasksNumber}
     * @param path        {@link org.otash.JsonDataWriter#PATH}
     */
    public JsonDataWriter(FakerApplicationGenerateRequest request , int tasksNumber , String path) {
        this.request = request;
        this.tasksNumber = tasksNumber;
        this.PATH = path;
    }

    /**
     * The method comes from {@link java.util.concurrent.RecursiveAction }.<br/><br/>
     * When invoke(ForkJoinTask) method is called from
     * {@link FakerApplicationGeneratorService#startActionWithJson(FakerApplicationGenerateRequest)},
     * this method is called recursively. And calculates whether {@link org.otash.JsonDataWriter#tasksNumber}
     * is less than {@link org.otash.JsonDataWriter#THRESHOLD} or not. If that's the case, json objects will be generated
     * by the method {@link org.otash.JsonDataWriter#generateDataAsJson(int , Set)}. Otherwise, 2 new {@link org.otash.JsonDataWriter}
     * objects are created with lesser number of tasks, and will be invoked. It continues until the case when the number of tasks
     * are less than or equal to {@link org.otash.JsonDataWriter#THRESHOLD}
     */
    @Override
    protected void compute() {

        Set<Field> fields = request.getFields();

        if ( tasksNumber <= THRESHOLD ) {
            generateDataAsJson(tasksNumber , fields);
        } else {
            int each = tasksNumber % 2 == 0 ? tasksNumber / 2 : tasksNumber / 2 + 1;

            JsonDataWriter helper1 = new JsonDataWriter(request , each , this.PATH);
            JsonDataWriter helper2 = new JsonDataWriter(request , each - 1 , this.PATH);

            invokeAll(helper1 , helper2);
        }
    }


    /**
     * @param rowsCount how many json objects should be created with types of {@link org.otash.FieldType}
     * @param fields    set of user-entered fields <br/><br/>
     *                  <p>
     *                  Firstly creates json objects and then inserts them into the file. If the file doesn't exist, creates one.
     */


    private void generateDataAsJson(int rowsCount , Set<Field> fields) {

//        synchronized ( FakerApplicationService.class ) {
        var result = new StringJoiner(",\n" , "" , "");
        for ( int i = 0 ; i < rowsCount ; i++ ) {
            var row = new StringJoiner(", " , "{" , "}");
            for ( Field field : fields )
                row.add(field.getPatternAsJson());
            result.add(row.toString());
        }
        Path path = Path.of(this.PATH);
        try {
            if ( Files.notExists(path) ) Files.createFile(path);
            Files.writeString(path , result.toString() , StandardOpenOption.APPEND);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
//        }
    }
}
