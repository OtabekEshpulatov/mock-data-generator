<h1 class="code-line" data-line-start=0 data-line-end=1 ><a id="MockDataGenerator_0"></a>Mock-Data-Generator</h1>
<p class="has-line-data" data-line-start="3" data-line-end="5">This is a fake data generator which supports SQL, JSON and CSV file<br>
formats to generate fake information upto millions of rows per request</p>
<h2 class="code-line" data-line-start=6 data-line-end=7 ><a id="Features_6"></a>Features</h2>
<ul>
<li class="has-line-data" data-line-start="8" data-line-end="9">Fast</li>
<li class="has-line-data" data-line-start="9" data-line-end="10">Supports JSON, SQL and CSV formats</li>
<li class="has-line-data" data-line-start="10" data-line-end="11">Lighweight</li>
</ul>
<p class="has-line-data" data-line-start="13" data-line-end="14">You can watch this video about the program efficiency <a href="https://youtu.be/GFxCQM5VIfQ">https://youtu.be/GFxCQM5VIfQ</a></p>
<h2 class="code-line" data-line-start=17 data-line-end=18 ><a id="Inner_Working_17"></a>Inner Working</h2>
<p class="has-line-data" data-line-start="19" data-line-end="22">Here is where I’m going to write about the inner workings of my program. Before all,<br>
I want to mention that there is  <a href="https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html">Java Fork/Join Framework</a> at the heart of the program<br>
to balance the workload between all processors by dividing a task into small subtasks.</p>
<p class="has-line-data" data-line-start="23" data-line-end="27">To use the program, user should define how  many rows of data he/she wants after selecting file format.<br>
And then the user need to choose field types and give names to them like “AGE userage”, “NAME username” and etc.<br>
After collectiong neccessary data, as I mentioned before, large tasks are divided into small ones like “Divide and conquer”<br>
paradigm. Here is what I mean:</p>
<p class="has-line-data" data-line-start="28" data-line-end="29">– Fork join Photo</p>
<p class="has-line-data" data-line-start="30" data-line-end="32">And of course my application itself is open source with a <a href="https://github.com/OtabekEshpulatov/mock-data-generator">public repoitory</a><br>
on GitHub.</p>
<h2 class="code-line" data-line-start=33 data-line-end=34 ><a id="Bot_version_33"></a>Bot version</h2>
<p class="has-line-data" data-line-start="35" data-line-end="39">As a developer, I need data to feed my code to check whether it’s functioning properly or not.<br>
So that, I created telegram bot version of it where you can go and use it for free. Here I’m leaving the link<br>
to the bot <a href="https://t.me/mockaroo_bot">https://t.me/mockaroo_bot</a>. Before going any further, you’re better to know how the<br>
bot actually works.</p>
<p class="has-line-data" data-line-start="40" data-line-end="41">All orders are collected in the form of query. So you need to grasp a little bit about querying.</p>
<img src="https://drive.google.com/file/d/1HLpDiaKPEHCeGwqVfa1hLHEqw6ACVM8o/view?usp=sharing">
