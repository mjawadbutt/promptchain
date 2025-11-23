


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
