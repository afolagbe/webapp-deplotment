def COLOR_MAP = [
    'SUCCESS':'good',
    'FAILURE':'danger'
]
pipeline{
    agent any
    tools {
        jdk 'JDK'
        maven 'maven'
    }
    environment {
        SNAPREPO = 'Vpro-snapshots'
        NEXUSUSER = 'admin'
        nexuspassword = 'admin'
        releaserepo = 'vpro-release'
        centralrepo = 'Vpro-mavan-central'
        nexusip = '172.31.30.167'
        nexusport = '8081'
        nexusgroup = 'Vpro-maven-group'
        nexuslogin = 'nexuslogin'
        SONAR_SCANNER = 'SonarQube Scanner'
        SONAR_SERVER = 'SonarQube Server'
    }
    stages {
        stage ('PULL THE APPLICATION FROM GITHUB') {
            steps {
                git branch: 'ci-jenkins', url: 'https://github.com/afolagbe/personal.git'
            }
        }
        stage ('BUILD THE APPLICATION') {
            steps {
                sh 'mvn -s settings.xml install -DskipTests' 
            }
            post{
                success{
                    echo 'Now archiving'
                    archiveArtifacts artifacts: '**/*.war'
                }
            }
        }
        stage ('TEST') {
            steps {
                sh ' mvn -s settings.xml test'
            }
            post{
                success {
                    slackSend channel: '#ci-project',
                    color:'good',
                    message: "TEST IS SUCCESS"
                }
                failure {
                    slackSend channel: '#ci-project',
                    color: 'danger',
                    message: "TEST IS FAILED"
                }
            }
        }
        stage ('UNIT TEST') {
            steps {
                sh 'mvn test'
            }
        }
        stage ('INTEGRATION TEST') {
            steps {
                sh 'mvn verify -DskipUnitTest'
            }
        }
        stage ('CHECKSTYLE ANALYSIS') {
            steps {
                sh 'mvn checkstyle:checkstyle'
            }
        }
        stage('update app properties'){
            steps{
                sh '''#JDBC Configutation for Database Connection
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://172.31.34.72:3306/accounts?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
jdbc.username=admin
jdbc.password=admin123

#Memcached Configuration For Active and StandBy Host
#For Active Host
memcached.active.host=172.31.34.72
memcached.active.port=11211
#For StandBy Host
memcached.standBy.host=127.0.0.2
memcached.standBy.port=11211

#RabbitMq Configuration
rabbitmq.address=172.31.34.72
rabbitmq.port=5672
rabbitmq.username=test
rabbitmq.password=test

#Elasticesearch Configuration
elasticsearch.host =192.168.1.85
elasticsearch.port =9300
elasticsearch.cluster=vprofile
elasticsearch.node=vprofilenode

EOT'''
            }
        }
    }
    post {
        always{
            echo 'slack notifications'
            slackSend channel: '#project',
            color: COLOR_MAP[currentBuild.currentResult],
            message: "*${currentBuild.currentResult}:* Job name ${env.JOB_NAME} build ${env.BUILD_NUMBER} time ${env.BUILD_TIMESTAMP} \n More info at: ${BUILD_URL}"
        }
    }
}
