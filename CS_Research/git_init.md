# How to initialize a git repo on the remote server
- Connect to the server
    - `ssh 10.102.67.32 -l drandall3`
    - enter password (the bit after the colon)
- Create git repo
    - `mkdir <name>`
    - `cd <name>`
    - `git init`
- Copy files
    - `scp <file> drandall3@10.102.67.32:/home/drandall3/<dir>
- Initial commit (remote)
    - `git config --global user.email "drandall3@ewu.edu"`
    - `git config --global user.name "Dustin Randall"`
    - `git add -A`
    - `git commit -m "initial commit"`

- Connect to the remote repo
    - git clone ssh://drandall3@10.102.67.32:/home/drandall3/<src>/.git [<repo name>]
    