# How to initialize a git repo on the remote server
- Connect to the server
    - `ssh drandall3@10.102.67.32`
    - enter password (the bit after the colon)
- Setup SSH keys
    - Generate a pub/private key
    - Local: `ssh-keygen -t ed25519 -C "dustin@windows"`
    - Copy key to the remote machine
    - Local: `scp $env:USERPROFILE\.ssh\id_ed25519.pub drandall3@10.102.67.32:/home/drandall3`
    - Write key to authorized_keys
    - Remote: `cat id_ed25519.pub >> .ssh/authorized_keys`
    - Update permissions
    - Remote: `chmod 600 .ssh/authorized_keys`
    - Test Connection
    - `ssh drandall3@10.102.67.32` (should not request password)
- Create git repo (Remote)
    - `mkdir <name>`
    - `cd <name>`
    - `git init`

- Connect to the remote repo (Local)
    - `git clone ssh://drandall3@10.102.67.32:/home/drandall3/<name>`
    - `cd <name>`
    - Create a working branch
    ` git switch -c durandal/work`
    - Make a change
    - `git add -A`
    - `git commit -m "Some message"`
    - `git push`
    - If you get an error about no upstream
    - `git config --global push.autoSetupRemote true`

- Pull changes into main (Remote)
    - `git fetch`
    - `git merge durandal/work main`