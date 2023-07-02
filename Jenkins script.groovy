Jenkins script
pipeline{
    agent any
    tools {
        maven 'maven'
        jdk 'JDK'
    }
    stages {
        stage(BUILD) {
            steps{
                sh 'mvn -s settings.xml install -DskipTests'
            }
        }
    }
}