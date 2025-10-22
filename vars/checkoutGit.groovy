def call(String gitBranch, String gitUrl, String credentialsId) {
    
    withCredentials([usernamePassword(credentialsId: 'github-cred', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_PAT')]) {
        checkout([
            $class: 'GitSCM',
            branches: [[name: "*/${gitBranch}"]],
            userRemoteConfigs: [[
                url: gitUrl, 
                credentialsId: 'github-cred'
            ]]
        ])
    }
}
