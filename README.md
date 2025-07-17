<div data-state="active" data-orientation="horizontal" role="tabpanel" aria-labelledby="radix-:r15:-trigger-preview" id="radix-:r15:-content-preview" tabindex="0" class="mt-2 ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2" style=""><div class="border border-border rounded-lg bg-background p-6 shadow-sm"><div class="prose prose-sm md:prose-base lg:prose-lg max-w-none prose-headings:font-bold prose-a:text-blue-600" style="user-select: none;"><div id="top" class="">

<div align="center" class="text-center">
<h1>ECOMMERCE-API</h1>
<p><em>Empowering Seamless Commerce, Accelerating Growth</em></p>

<img alt="last-commit" src="https://img.shields.io/github/last-commit/Pedrooliveira465/ecommerce?style=flat&amp;logo=git&amp;logoColor=white&amp;color=0080ff" class="inline-block mx-1" style="margin: 0px 2px;">
<img alt="repo-top-language" src="https://img.shields.io/github/languages/top/Pedrooliveira465/ecommerce?style=flat&amp;color=0080ff" class="inline-block mx-1" style="margin: 0px 2px;">
<img alt="repo-language-count" src="https://img.shields.io/github/languages/count/Pedrooliveira465/ecommerce?style=flat&amp;color=0080ff" class="inline-block mx-1" style="margin: 0px 2px;">
<p><em>Built with the tools and technologies:</em></p>
<img alt="Spring" src="https://img.shields.io/badge/Spring-000000.svg?style=flat&amp;logo=Spring&amp;logoColor=white" class="inline-block mx-1" style="margin: 0px 2px;">
<img alt="MySQL" src="https://img.shields.io/badge/MySQL-4479A1.svg?style=flat&amp;logo=MySQL&amp;logoColor=white" class="inline-block mx-1" style="margin: 0px 2px;">
<img alt="Docker" src="https://img.shields.io/badge/Docker-2496ED.svg?style=flat&amp;logo=Docker&amp;logoColor=white" class="inline-block mx-1" style="margin: 0px 2px;">
<img alt="XML" src="https://img.shields.io/badge/XML-005FAD.svg?style=flat&amp;logo=XML&amp;logoColor=white" class="inline-block mx-1" style="margin: 0px 2px;">
</div>
<br>
<hr>
<h2>Table of Contents</h2>
<ul class="list-disc pl-4 my-0">
<li class="my-0"><a href="#overview">Overview</a></li>
<li class="my-0"><a href="#getting-started">Getting Started</a>
<ul class="list-disc pl-4 my-0">
<li class="my-0"><a href="#prerequisites">Prerequisites</a></li>
<li class="my-0"><a href="#installation">Installation</a></li>
<li class="my-0"><a href="#usage">Usage</a></li>
<li class="my-0"><a href="#testing">Testing</a></li>
</ul>
</li>
</ul>
<hr>
<h2>Overview</h2>
<p>ecommerce-api is a comprehensive backend solution tailored for building scalable and secure e-commerce platforms. It leverages Spring Boot, JWT authentication, and MySQL to deliver a reliable, maintainable, and well-documented API.</p>
<p><strong>Why ecommerce-api?</strong></p>
<p>This project simplifies the development of a secure and scalable e-commerce backend. The core features include:</p>
<ul class="list-disc pl-4 my-0">
<li class="my-0"><strong>🛠️</strong> <strong>Security &amp; Authentication:</strong> Implements JWT-based security and role management for protected API access.</li>
<li class="my-0"><strong>🚢</strong> <strong>Containerized Environment:</strong> Uses Docker Compose for consistent, easy-to-deploy database setup.</li>
<li class="my-0"><strong>🧪</strong> <strong>Robust Testing:</strong> Extensive unit tests ensure reliability across services, controllers, and security components.</li>
<li class="my-0"><strong>📄</strong> <strong>API Documentation:</strong> Integrates Swagger/OpenAPI for clear, automated API docs.</li>
<li class="my-0"><strong>🔧</strong> <strong>Modular Data Handling:</strong> Uses DTOs and custom exceptions for clean, maintainable code.</li>
<li class="my-0"><strong>⚙️</strong> <strong>Core Functionalities:</strong> Supports data persistence, validation, and business logic essential for e-commerce operations.</li>
</ul>
<hr>
<h2>Getting Started</h2>
<h3>Prerequisites</h3>
<p>This project requires the following dependencies:</p>
<ul class="list-disc pl-4 my-0">
<li class="my-0"><strong>Programming Language:</strong> Java</li>
<li class="my-0"><strong>Package Manager:</strong> Maven</li>
<li class="my-0"><strong>Container Runtime:</strong> Docker</li>
</ul>
<h3>Installation</h3>
<p>Build ecommerce-api from the source and install dependencies:</p>
<ol>
<li class="my-0">
<p><strong>Clone the repository:</strong></p>
<pre><code class="language-sh">❯ git clone https://github.com/Pedrooliveira465/ecommerce.git
</code></pre>
</li>
<li class="my-0">
<p><strong>Navigate to the project directory:</strong></p>
<pre><code class="language-sh">❯ cd ecommerce-api
</code></pre>
</li>
<li class="my-0">
<p><strong>Install the dependencies:</strong></p>
</li>
</ol>
<p><strong>Using <a href="https://www.docker.com/">docker</a>:</strong></p>
<pre><code class="language-sh">❯ docker build -t Pedrooliveira465/ecommerce .
</code></pre>
<p><strong>Using <a href="https://maven.apache.org/">maven</a>:</strong></p>
<pre><code class="language-sh">❯ mvn install
</code></pre>
<h3>Usage</h3>
<p>Run the project with:</p>
<p><strong>Using <a href="https://www.docker.com/">docker</a>:</strong></p>
<pre><code class="language-sh">docker run -it {image_name}
</code></pre>
<p><strong>Using <a href="https://maven.apache.org/">maven</a>:</strong></p>
<pre><code class="language-sh">mvn exec:java
</code></pre>
<h3>Testing</h3>
<p>Ecommerce-api uses the {<strong>test_framework</strong>} test framework. Run the test suite with:</p>
<p><strong>Using <a href="https://www.docker.com/">docker</a>:</strong></p>
<pre><code class="language-sh">echo 'INSERT-TEST-COMMAND-HERE'
</code></pre>
<p><strong>Using <a href="https://maven.apache.org/">maven</a>:</strong></p>
<pre><code class="language-sh">mvn test
</code></pre>
<h3>MySQL Dump</h3>
<p>This project includes a <code>mysql-dump.sql</code> file containing the database schema and initial data.</p>
<p>You can import this dump into your MySQL server to quickly set up the database:</p>
<pre><code class="language-sh">mysql -u &lt;username&gt; -p &lt;database_name&gt; &lt; mysql-dump.sql
</code></pre>
<p>Replace <code>&lt;username&gt;</code> and <code>&lt;database_name&gt;</code> with your MySQL credentials and database name.</p>
<p>This dump facilitates consistent database setup during development or deployment.</p>

<hr>
<div align="left" class=""><a href="#top">⬆ Return</a></div>
<hr></div></div></div></div>
