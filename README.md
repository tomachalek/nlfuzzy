nlfuzzy
=======

"Nlfuzzy" is a [NetLogo](http://ccl.northwestern.edu/netlogo/) extension which acts as a a simple middleware between NetLogo and [jFuzzyLogic](http://jfuzzylogic.sourceforge.net/html/index.html) library. It allows you to define fuzzy variables and rules for NetLogo agents (turtles, patches) using [Fuzzy Control Language](http://jfuzzylogic.sourceforge.net/html/manual.html#details) (see [full FCL specification](http://www.fuzzytech.com/binaries/ieccd1.pdf) for more information).

Download the extension
----------------------

Precompiled version can be downloaded [here](https://s3-eu-west-1.amazonaws.com/tomdata/nlfuzzy/nlfuzzy.zip).

How to build nlfuzzy
--------------------

If you want to build *nlfuzzy* yourself, then your computer has to meet the following requirements:

* installed NetLogo 5.1 (5.0.x should work too) or at least *NetLogo.jar* library from the installation package,
* installed Java Development Kit (JDK) of at least version 6 (currently it is better to download version 8 which is backward compatible; see [downloads at Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html)),
* installed [Apache Maven](https://maven.apache.org/).

Many Java libraries are available via public Maven repositories but in some cases, however, this is not true. This holds for both *NetLogo* and *jFuzzyLogic* which must be downloaded manually and installed as local Maven dependencies. Thanks to this, Maven will be able to find all the necessary dependencies defined in *pom.xml* file.

### Install NetLogo.jar as a local Maven dependency

In case you have Windows Vista/7/8, open a command line window (*cmd* or *powershell*), change your working directory (command *cd*) to the downloaded and unpacked source code directory (i.e. the directory where file *pom.xml* is located) and type:

```
mvn install:install-file -Dfile="C:/Program Files (x86)/NetLogo 5.1.0/NetLogo.jar" -DgroupId=org.nlogo -DartifactId=netlogo -Dversion=5.1.0 -Dpackaging=jar
```

Parameter *-Dfile* must contain a correct path to *NetLogo.jar* according to where you installed NetLogo.

### Install jFuzzyLogic as a local Maven dependency

Download the *core* version of *jFuzzyLogic* from the [project site](http://jfuzzylogic.sourceforge.net/html/index.html) and from nlfuzzy's project directory run:

```
mvn install:install-file -Dfile="C:/Users/JaneDoe/Downloads/jFuzzyLogic_core.jar" -DgroupId=net.sourceforge -DartifactId=jFuzzyLogic -Dversion=3.0 -Dpackaging=jar
```

Again, path to the respective *jar* file must be correct.

### Build the project

Finally, you can compile and package the project:

```
mvn clean dependency:copy-dependencies package
```

How to install the extension
----------------------------

If you have built *nlfuzzy* **from sources** then copy the *nlfuzzy.jar* (located in the *./target* directory) and *jFuzzyLogic.jar* (located in the *./target/dependency* directory) files to your NetLogo installation directory's *extensions/nlfuzzy* subdirectory (*extensions* is already there but *nlfuzzy* has yet to be created).

In case you have downloaded the **compiled version**, just copy the *nlfuzzy* directory to the *extensions* directory.

If you have a running NetLogo instance then you must exit the program and run it again.

How to use the extension
------------------------

### nlfuzzy:init

Before attaching any rules or evaluating any variables, an *nlfuzzy:init* procedure must be called (typically in the "setup" function - see the example below).

```
to setup
  nlfuzzy-init
end
```

### nlfuzzy:use-fcl

Nlfuzzy is able to operate with multiple rule sets. Each rule set is defined using a single *FCL*
file. Rule file is attached to an agent via function *nlfuzzy:use-fcl path_to_a_file.fcl*.

```
ask turtles [
  nlfuzzy-use-fcl "my-fuzzy-rules.fcl"
]
```

### nlfuzzy:set-value

Input variables (defined in your FCL file) are set via function *nlfuzzy:set-value "fuzzy-variable-name" "a-value-to-be-set".

```
ask turtles [
  nlfuzzy:set-value "some_fuzzy_variable" "a_value_to_be_assigned"
]
```

### nlfuzzy:eval

This function evaluates defined rules for passed output variable name. E.g.:

```
ask turtles [
  set speed nlfuzzy:eval "speed"
]
```
 
A skeleton of a nlfuzzy-based program may look like this:

```
extensions [ nlfuzzy ]

to setup
  nlfuzzy:init
  ask agents [
    nlfuzzy:use-fcl "my_fcl_rules.fcl"
end

to go
  ...
  ask turtles [
    nlfuzzy:set-value "some_fuzzy_variable" "a_value_to_be_assigned"
  ]
  ...
  ask turtles [
    set some-variable (nlfuzzy-eval "output_variable")
  ]
]
```

A simple working demo showing how to use *nlfuzzy* can be found in the *demo* directory. 
