# -----------------------------------------------------------------------------
# It contains common environment variables to all scripts launching clients
# and servers. It must be inclued by all of them.
# -----------------------------------------------------------------------------

# ----------------------------- Run-time system -------------------------------

# The following environment variables must be adapted to your environment:
# - PROJECT_HOME

# Java virtual machine.
JAVA_VIRTUAL_MACHINE=$JAVA_HOME/bin/java

# EmbeddedProject.
PROJECT_HOME=/home/jose/workspace/EmbeddedServer

# --------------------------- CORBAWS-JavaExamples ----------------------------

# A variable with the same notation as CLASSPATH containing only the classpath
# for the infraestructure used (CORBA and Web Services).


for i in $PROJECT_HOME/lib/*.jar
do
    PROJECT_REQUIRED_CLASSPATH=$PROJECT_REQUIRED_CLASSPATH:$i
done


# A variable defined by convenience to run classes with a main() method from
# the command line.
PROJECT_CLASSPATH=$PROJECT_REQUIRED_CLASSPATH:\
$PROJECT_HOME/target/classes
# Export PROJECT_CLASSPATH to execute unit tests from the
# command line.
export PROJECT_CLASSPATH
