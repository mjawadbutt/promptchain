
GIT SETUP:

Update the 'init' config in global GIT config (in 'program files\git\etc\gitconfig') so that the default branch
is 'main' instead of 'master' (to make it compatible with Github):

[init]
defaultBranch = main

Note that we can use git --global <property> <value> command to change any GIT config property value also instead
of directly modifying any git config file (as above). Both options work fine.

When GIT is installed on windows (or mac or linux as well) it MAY install a global credentials helper by default.

To check if you have one:
git credential-manager-core --version

You should see manager-core because GCM Core is included by default since Git v2.28.

To see if enabled globally:
git config --global credential.helper

If not enabled globally, then check at repo level:
Go to the project root dir (where the '.git' dir is)
git config credential.helper

If not enabled, then type:
git config --global credential.helper manager-core

Its official name is Git Credential Manager. It is a (which is the cross-platform credentials manager which uses
OS native storage (Windows: Windows Credential Manager, macOS: macOS Keychain Linux: GNOME Keyring or libsecret). 
It encrypts stored credentials and automatically prompts for login (via browser or terminal) when needed. It works
with GitHub (supports fine-grained PATs, OAuth, GPG tokens), Azure DevOps, Bitbucket, GitLab (with some limitations) 
so that the authentication can be performed without the need to send the password over the network or store
it in any file.

Also that no IDE (eg: IntelliJ) specific config is needed for GIT to repo authentication (github, bitbucket, etc.).
The authentication needs only to be configured in GIT. The IDEs just use GIT CLI and need not know or do anything 
about authentication. Also in IntelliJ settings for GIT, leave the use credential manager checkbox unchecked
because checking that mean IntelliJ will start using its own native credential manager which we do not want.



GIT TO GITHUB.COM SETUP:

Generate a fine-grained Personal Access Token (PAT) token from github.com. The token should have permissions to
write to the repository and also to be able to publish snapshot and release artifacts (which in our case would be a 
docker image instead of a jar). Add this token in <repositories> in ./m2/settings.xml so that GIT can login to
your Github account.



GPG KEY SETUP:

Make personal GPG Key which you can use to digitally sign your GIT commits. 

Download and install the gpg key generator/manager/agent tool:
Windows: https://gpg4win.org

Configure GIT to use this tool for retrieving the key detail using a fingerprint:
git config --global gpg.program "C:\Program Files (x86)\GnuPG\bin\gpg.exe"

Note that the above gpg.program step is necessary to make the key usable in IntelliJ IDEA.

The machine will have to be restarted after installing this tool.

Open a command shell and do:
gpg --full-generate-key

At the prompts enter as you like, eg:
Key type (RSA and RSA)
Key size (4096 recommended)

Expiry can be set to any value. Also, if a key expires, it can be renewed i.e. we don't have to discard it.
However, if we delete/discard a key, then any commits that used it will become unverifiable, which is not an issue
but defeats the purpose of using GPG keys. So its better to renew rather than delete and create new one.

Display the full fingerprint (i.e. in long format) using:
gpg --list-secret-keys --keyid-format LONG

Note that the fingerprint is just a hash of the public key.

Copy the full fingerprint to clipboard.

Set the fingerprint in the global GIT:
git config --global user.signingkey <fingerprint>
git config --global commit.gpgsign true

To check any GIT config value, use: git --global <property>
For example:
git config --global gpg.program

To verify that the key is setup properly:

Open 'gitbash' shell:
gitbash

Type:
echo "test" | gpg --clearsign

Now add the public key (which is the public key, not the fingerprint, which is the private key) to Github so that it can
verify your signatures:

gpg --armor --export m_jawad_butt@yahoo.com

Copy the public key to clipboard, goto github.com, navigate to new GPG key option, and paste it in the textbox there.
The above public key can also be upload to a keyserver (e.g., keys.openpgp.org) so others can find it.

To test a signed commit:
git commit -S -m "Test signed commit"

ALL DONE.
