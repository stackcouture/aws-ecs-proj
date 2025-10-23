Snyk Install 
apt-get update
curl https://static.snyk.io/cli/latest/snyk-linux -o snyk
chmod +x ./snyk
mv ./snyk /usr/local/bin/

TOken Generate in Snyk 
* Go to ‘Snyk,’ create your account, then navigate to the dashboard. On the left-side menu bar,   
  click on your account name. From there, click on ‘Account Settings’.
* After entering, proceed to the ‘General’ tab. You will find the ‘Generate Auth Token’ option  
  there; click on it and copy the token. Save the token in Notepad. In my case, I have already created that token.
* snyk auth <Token>