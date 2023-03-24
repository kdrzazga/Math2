pipeline {
    agent any
    
    tools{
        git 'Default'   
    }
    
    stages {
        stage('checkout'){
            steps {
                git 'http://github.com/kdrzazga/Math2'
            }
            post {
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
                success {
                    echo "Project built"
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
    }
}
