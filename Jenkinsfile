pipeline {
    agent any
    
    tools{
        git 'Default'   
    }
    
    triggers {
        cron('15 0 * * *')   
    }
    
    stages {
        stage('checkout'){
            steps {
                git 'http://github.com/kdrzazga/Math2'
            }
            post {
                always {
                    echo 'Checout finished.'
                }
                success {
                    echo "Project checked out from GH"
                }
            }
        }
        stage('build') {
            steps {
                sh "mvn -Dmaven.test.failure.ignore=true clean build"
            }
            post {
                failure {
                    echo "Build failed"
                }
            }            
        }
        stage('test'){
            steps {
                sh "mvn -Dmaven.test.failure.ignore=true test"
            }
            post {
                success {
                    echo "Tests passed"
                }
            }
        }
        stage('deploy'){
            steps {
                script {
                    deploy
                }
                
            }
        }
    }
}
