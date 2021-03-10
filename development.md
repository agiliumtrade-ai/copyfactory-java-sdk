# Release checklist
1. Verify changelog.md
2. Make sure pom.xml version is correctly updated
3. Make sure SDK version is updated in the readme installation section
4. Make sure SDK version is updated in examples
5. Verify examples are in working condition
6. Verify javadoc is compiled without errors
7. Publish to Maven Central
8. Create git tag

# Publishing to Maven Central

## Creating a repository in Maven Central

1. If you don't have a JIRA account, [create it here](https://issues.sonatype.org/secure/Signup!default.jspa).
2. If this is the first publishing and a repository in Maven Central for the project does not exist yet, create a new project ticket [here](https://issues.sonatype.org/secure/CreateIssue.jspa?issuetype=21&pid=10134).
There you will need to fill a form with these fields:
    
    - Summary: `Create a repository for CopyFactory Java SDK` _(or something like that)_
    - In description (not required) you may describe the project.
    - Group Id: `cloud.metaapi.sdk` (it is important that this id matches `groupId` in `pom.xml`)
    - Project URL: [https://metaapi.cloud](https://metaapi.cloud)
    - SCM url: [https://github.com/agiliumtrade-ai/copyfactory-java-sdk.git](https://github.com/agiliumtrade-ai/copyfactory-java-sdk.git)
    - Already Synced to Central: `No`

The status of the ticket (issue) will be `Open`. It may take some time until the issue is `Resolved`. Check comments of the issue. You may be asked to do something (e.g. to verify github account ownership).

## Generatig keys for sigining files

1. If GnuPG is not installed on your computer, follow steps in the [installation section](https://central.sonatype.org/pages/working-with-pgp-signatures.html#installing-gnupg). Note that on some systems the newer `gpg2` will be used.
2. If you have not generated keys, follow steps in [this section to generating a key pair](https://central.sonatype.org/pages/working-with-pgp-signatures.html#generating-a-key-pair). Note that in that section is described command `gpg --full-gen-key` instead of `gpg --gen-key`. You can use any of these commands, but `gpg --full-gen-key` asks for more settings. **It is important to remember passphrase (it will be used later).**
3. If you have not distributed public key, follow steps in the [distributing](https://central.sonatype.org/pages/working-with-pgp-signatures.html#distributing-your-public-key) section. 

    - Synchronization of your key between servers may take some time. You can also execute `gpg --keyserver hkp://keyserver.ubuntu.com --send-keys <your_key>` to send the key right to one of the servers which will be participated in the next operations.

## Deploying to the repository

1. Make sure that you have a file `%USER_HOME%/.m2/settings.xml` with contents as below. Instead of `your-jira-id` write your JIRA username and instead of `your-jira-pwd` write your JIRA password.

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-jira-id</username>
      <password>your-jira-pwd</password>
    </server>
  </servers>
</settings>
```

2. Go to the root of the project and execute `mvn clean deploy -P release`. This command can ask for a passphrase of the key that was generated earlier.
3. Wait until build successfully completed. From this point the project should be able to be found in [Nexus Repository Manager](https://oss.sonatype.org) and in [Maven Central Repository Search](https://search.maven.org).