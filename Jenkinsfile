//deklarasi variable
def ImageName = ""
def ImageNameDev = ""
def ImageNameProd = ""
def imageTag = ""
def Namespace = ""

pipeline {
    agent any

    stages{
        //checkout from git.i3datacenter.com
        stage('Checkout'){
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '**']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '8911ee99-c456-4c8a-82a8-91f37c9a8292', refspec: '+refs/tags/*:refs/remotes/origin/tags/*', url: 'http://git.i3datacenter.com/alan/jaguar-java.git']]])
                script {
                    ImageName = "nexus.i3datacenter.com:8083/jaguar-java"
                    ImageNameDev = "nexus.i3datacenter.com:8083/jaguar-java-dev"
                    ImageNameProd = "nexus.i3datacenter.com:8083/jaguar-java-prod"
                    sh "/root/add-content/ver_script"
                    imageTag = readFile('.git/commit-id').trim()
                    Namespace = "dev-java"
                }
            }
        }

        //Build Jaguar Java Apps
        stage('Build maven'){
            steps{
                sh '''
                mvn clean install
                '''
            }
        }

        //Deploy Jar to Nexus
        stage('Deploy Jar to Nexus'){
            steps{
                sh '''
                mvn deploy
                '''
            }
        }
               
        //Build Image Docker and Tag Image to Dev,Prod
        stage('Build Docker Image'){
            steps{
                sh "mvn docker:build"
                sh "docker tag nexus.i3datacenter.com:8083/jaguar-java ${ImageNameDev}:${imageTag}"
                sh "docker tag nexus.i3datacenter.com:8083/jaguar-java ${ImageNameProd}:${imageTag}"

            }
        }

        //Build Image Docker
        stage('Push Docker Image'){
            steps{
                sh "docker push ${ImageName}:latest"
                sh "docker push ${ImageNameDev}:${imageTag}"          
                sh "docker push ${ImageNameProd}:${imageTag}"          
            }
        }
        
        stage('Deploy'){
            input {
                message "Apakah akan di Deploy ke K8s-DEV ?"
                ok "Yes, Deploy"
            }
            steps{
                //clone helm chart from gitlab 
                echo "Git Clone Playbook Maven Docker" 
                //
                sh "cd /root/playbooks && rm -rf playbook-mavendocker && git clone git@git.i3datacenter.com:alan/playbook-mavendocker.git"
                        
                //create env helm chart
                echo "Running Playbook for git clone Helm Chart to K8s cluster"
                sh "ansible-playbook /root/playbooks/playbook-mavendocker/clone-repo-mavendocker.yaml"

                //deploy to Dev
                sh "ansible-playbook /root/playbooks/playbook-mavendocker/deploy-dev.yml  --user=root --extra-vars ImageNameDev=${ImageNameDev} --extra-vars imageTag=${imageTag} --extra-vars Namespace=${Namespace}"
                }
                post {
                    success {
                        echo 'Code Deployed to K8s-Dev'
                    }
                    failure {
                            echo 'Deployment Failed to K8s-Dev'
                        }
                    }
                    
                }
        }

    }