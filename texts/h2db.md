# Notes on the H2 database

## Starting the "remote" H2

To start the H2 database in remote mode, (see [Using the Server](http://h2database.com/html/tutorial.html?highlight=JDBC_URL&search=JDBC#using_server)), run

~~~
java -cp ${PATH_TO}/h2.jar org.h2.tools.Server &
~~~

in the shell. That is "put `h2.jar` on the Java classpath, execute class `org.h2.tools.Server` and the shell will put the process into the
set of processes running in the background as demanded by `&`.

There is no need to use the `nohup` command to make the process immune to closing of the terminal. The process seems to be immune already.

An icon my appear in the system tray. It allows you to shut the database down or start a new browser to connect to the H2 console.

The H2 database process will listen on three ports, one for each TCP server it is running internally, on of which is the console web server on port 8082.
Each can be switched off via appropriate command line options.

- 9092: TCP for JDBC. Out-of-process JDBC client can connect here
- 5435: TCP for PostgresSQL client protocol. You can use a PostgreSQL client to connect to the H2 database here (I think this is useful for when you want to connect with ODBC, as an ODBC-PostgresSQL bridge can be used here)
- 8082: "Web Console": You get HTTP on this port. You can connect with the browser to this endpoint to run your queries. Use http://localhost:8082/ to connect.

Of course you must have downloaded `h2.jar` first, generally as something like `h2-2.2.220.jar`, which you can then rename or symlink to.

In my case, `h2-2.2.220.jar` had been downloaded by Gradle via the IDE and was available in the Gradle cache as:

~~~
~/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.2.220/afd532e4f29d309ae053e57c853916551c777807/h2-2.2.220.jar
~~~

Thus:

~~~
mkdir -p ~/Spring/h2
cp ~/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.2.220/afd532e4f29d309ae053e57c853916551c777807/h2-2.2.220.jar ~/Spring/h2
ln -s ~/Spring/h2/h2-2.2.220.jar ~/Spring/h2/h2.jar
PATH="~/Spring/h2"
~~~

And then you can just `java -cp ${PATH_TO}/h2.jar org.h2.tools.Server`

This command takes `-?` to list options:

~~~
$ java -cp h2.jar org.h2.tools.Server -?
Starts the H2 Console (web-) server, TCP, and PG server.
Usage: java org.h2.tools.Server <options>
When running without options, -tcp, -web, -browser and -pg are started.

 Options are case sensitive.
Supported options[-help] or [-?]Print the list of options
[-web]                  Start the web server with the H2 Console
[-webAllowOthers]       Allow other computers to connect - see below
[-webExternalNames]     The comma-separated list of external names and IP addresses of this server, used together with -webAllowOthers
[-webDaemon]            Use a daemon thread
[-webPort <port>]       The port (default: 8082)
[-webSSL]               Use encrypted (HTTPS) connections
[-webAdminPassword]     Password of DB Console administrator
[-browser]              Start a browser connecting to the web server
[-tcp]                  Start the TCP server
[-tcpAllowOthers]       Allow other computers to connect - see below
[-tcpDaemon]            Use a daemon thread
[-tcpPort <port>]       The port (default: 9092)
[-tcpSSL]               Use encrypted (SSL) connections
[-tcpPassword <pwd>]    The password for shutting down a TCP server
[-tcpShutdown "<url>"]  Stop the TCP server; example: tcp://localhost
[-tcpShutdownForce]     Do not wait until all connections are closed
[-pg]                   Start the PG server
[-pgAllowOthers]        Allow other computers to connect - see below
[-pgDaemon]             Use a daemon thread
[-pgPort <port>]        The port (default: 5435)
[-properties "<dir>"]   Server properties (default: ~, disable: null)
[-baseDir <dir>]        The base directory for H2 databases (all servers)
[-ifExists]             Only existing databases may be opened (all servers)
[-ifNotExists]          Databases are created when accessed
[-trace]                Print additional trace information (all servers)
[-key <from> <to>]      Allows to map a database name to another (all servers)
The options -xAllowOthers are potentially risky.

 For details, see Advanced Topics / Protection against Remote Access.
See also https://h2database.com/javadoc/org/h2/tools/Server.html
~~~

An alternative command to run the remote H2 server is:

~~~
java -cp h2.jar org.h2.tools.GUIConsole &
~~~

In this case, the browser will additionally be started, with the URL of the Web console: http://localhost:8082/

## Web Console

The H2 process integrates a Web Server that presents a GUI to manage the database and issue queries.

Point your browser to http://localhost:8082/

See also the JavaDoc for https://h2database.com/javadoc/org/h2/tools/GUIConsole.html

## Stopping the "remote" H2

To stop the H2 "remote mode" database process, either use the menu on the icon in the system try or send a signal to the process:

Determine the process id of the H2 process (maybe using `lsof`, see below), then is this in the shell:

~~~
kill <PID>
~~~

Where <PID> is the process number.

This sends the `TERM` signal to the process with number <PID> and it will perform cleanup tasks (like flushing buffers) and exit.

If the process is still running in the terminal foreground, CTRL-C should do the trick too.

## Database settings

I have found this:

http://h2database.com/javadoc/org/h2/engine/DbSettings.html?highlight=properties&search=properties

But there must be another page.

A lot of settings can be found in table `INFORMATION_SCHEMA.SETTINGS`. In the web console:

~~~
SELECT * FROM INFORMATION_SCHEMA.SETTINGS 
~~~

SQL commands to manipulate specific settings exist. See here: http://h2database.com/html/commands.html

## Ports in use on H2 process in "remote" mode

As mentioned the following ports are in use:

- 9092: TCP for JDBC. Out-of-process JDBC client can connect here
- 5435: TCP for PostgresSQL client protocol. You can use a PostgreSQL client to connect to the H2 database here (I think this is useful for when you want to connect with ODBC, as an ODBC-PostgresSQL bridge can be used here)
- 8082: "Web Console": You get HTTP on this port. You can connect with the browser to this endpoint to run your queries. Use http://localhost:8082/ to connect.

Listing port activity using the `lsof` command on Linux is done with:

~~~
lsof -P -i :8082,5435,9092
~~~

"List processes that use IP ports 8082, 5434, 9092."

Below you see process 79661 (started as "java") listening on those ports. That's the H2 database in remote mode.

~~~
$ lsof -P -i :8082,5435,9092
COMMAND   PID USER   FD   TYPE DEVICE SIZE/OFF NODE NAME
java    79661 user    7u  IPv6 686797      0t0  TCP *:8082 (LISTEN)
java    79661 user   18u  IPv6 686811      0t0  TCP *:9092 (LISTEN)
java    79661 user   20u  IPv6 686818      0t0  TCP *:5435 (LISTEN)
~~~



