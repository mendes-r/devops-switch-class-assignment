# Class Assignment 1 Report

Topic of this assignment: Version Control with Git

## 1. Analysis, Design and Implementation

### Git, a distributed version control system

[Git](https://git-scm.com/) is a free and open source distributed version control system. It was created by Linus Torval in 2005 for the development of the Linus Kernel and is currently the most used version control system for small and big businesses.

### Assignment

Implementation of a simple scenario illustrating a Git workflow and the same implementation with an alternative version control system.

The scenario involves the development of a new feature.

>New feature: add a new email-field to the application

### 1.1 Confirm that you are on the master branch

```console
❯ git status
No ramo master
Your branch is up to date with 'origin/master'.
não há nada para submeter, árvore de trabalho limpa
```

This command shows the working tree status.
As seen above, we are currently on the master branch and are up to date with the remote master.

### 1.2 Tag the stable version in the master branch
  
```console
❯ git tag -a v1.2.0 -m "initial version"
```

This command creates a tag object and assigns it to the last commit. The _-a_ flag makes an annotated tag, and the _-m_ flag enables you to add a message to it.

If we want to tag a particular commit, it can be done by specifying the unique hash of that commit.

```console
❯ git tag -a v1.2.0 <hash> -m "initial version"
```

### 1.3 Push the tag to the remote repository
  
```console
❯ git push origin v1.2.0
```

_origin_ is the default name Git gives to the cloned server.

### 1.4 Create a new branch named "email-field" to implement the new feature

```console
❯ git checkout -b email-field
```

The flag _-b_ creates a new branch with a designated name.
Here the _git checkout_ command guarantees that after the branch is created we will switch to it.

An alternative would be the following:

```console
❯ git branch email-field
❯ git checkout email-field
```

First we create a branch with the command _git branch_, then we switch to it by using the _git checkout_ command.

A quick _git status_ will confirm this.

```console
❯ git status
No ramo email-field
Your branch is up to date with 'origin/email-field'.
não há nada para submeter, árvore de trabalho limpa
```

To push the new branch to the remote repository we need to run the following command:

```console
❯ git push --set-upstream origin email-field
```

### 1.5 Implement new feature: test, implementation and debugging

Following a test-driven development, some tests were created to simulate the required behavior of the new feature.
The new feature was then implemented in the server and client side, using debugging tools to detect and remove existing and potential errors in the code.

### 1.6 Add changes to the staging area
  
```console
❯ git add <file>
```

The command adds a given file, found in the working directory, to the staging area. By doing so, it prepares the file for the next commit.

A _git status_ will confirm this.

```console
❯ git status
No ramo master
Your branch is up to date with 'origin/master'.
Alterações para serem submetidas:
(use "git restore --staged <file>..." to unstage)
modificado:    <file>
```

### 1.7 Commit changes to the local repository

```console
❯ git commit -m "commit message here"
```

We can also use the flag _-a_ to tell the command to automatically stage all the files that have been modified and deleted - except untracked files.
Essentially it enables you to skip the chapter 1.6.

### 1.8 Incorporate changes from the remote repository into the current branch

The best practices advise us to make a _pull_ before _pushing_ any changes to the remote repository. It ensures the correct handling of possible merge conflicts.

```console
❯ git pull
```

In its default mode, _git pull_ is shorthand for:
  
```console
❯ git fetch
❯ git merge FETCH_HEAD
```

The fetch command only downloads what is new in the remote repository without integrating any of the new data to the working directory.

### 1.9 Push local commits to remote repository

Finally we push our commit to the remote repository:

```console
❯ git push origin email-field
```

### 1.10 Merge email-field branch with the master

First we need to change to the master branch by using the _checkout_ command without any flags, and by specifying the name of the branch to where we want to switch. In this case, the master branch.

```console
❯ git checkout master
```

Merge _email-field_ branch with the master branch

```console
❯ git merge email-field
```

### 1.11 Tag the stable version in the master branch

```console
❯ git tag -a v1.3.0 -m "email field feature"
```

### 1.12 Push the tag to the shared server

```console
❯ git push origin v1.3.0
```

### 1.13 After identifying the bug in the system, create a new branch called fix-invalid-email to fix it

```console
❯ git checkout -b fix-invalid-email
```

A quick _git status_ will confirm again that we are now on the new branch.

```console
❯ git status
No ramo fix-invalid-email
Your branch is up to date with 'origin/email-field'.
não há nada para submeter, árvore de trabalho limpa
```

Push the new branch to origin, aka remote repository:

```console
❯ git push --set-upstream origin fix-invalid-email
```

### 1.14 Fix the bug

Adding more tests to narrow the source of the bug.
We want to accept only valid email addresses, i.e. not null, not empty, and with a given format; a single "@" sign must be included.

### 1.15 Commit the changes to the local repository

```console
❯ git commit -a -m "commit message here"
```

We use here the _-a_ flag to "jump" directly from the working directory to the local repository. This must be made with a good degree of certainty. There is the danger of committing unwanted files.

### 1.16 Push local commits to remote repository

```console
❯ git push
```

### 1.17 Merge fix-invalid-email branch with the master

```console
❯ git checkout master
❯ git merge fix-invalid-email
```

To delete the no more needed branch use the following command:

```console
❯ git branch -d fix-invalid-email
```

To delete the branch on the remote repository:

```console
❯ git push origin --delete fix-invalid-email
```

In Git, deleting a branch is almost like erasing a part of your past. An honest project is a project with all branches visible. However, deleting branches helps facilitate the reading of the project's history by "cleaning" "unimportant" deviations during the development.

### 1.18 Tag the stable version in the master branch and push it to origin
  
```console
❯ git tag -a v1.3.1 -m "fix invalid email bug"
❯ git push origin v1.3.1
```

***

## 2. Analysis of an Alternative

There are many alternatives to Git inside the distributed version control system family. The most commonly used is [Mercurial](https://www.mercurial-scm.org/).

There is also another family of version control system known has centralized. [Subversion](https://subversion.apache.org/), or SVN for short, is one of the options. It is also the most widely used centralized version control system.

As its name suggests, a centralized version control system is based on the idea that there is only a single central copy of the project somewhere, but most likely, on a server.

Git, as a counterpart, saves a complete copy of the project on each developers machine. This copy has the full history of the project. The obvious disadvantage of the Git approach is the waste of disk space, but the redundancy guarantees that there is always a perfect backup of the project somewhere. Another advantage is the possibility to work on the project even when the server is offline or you don't have access to the internet.

Creating branches in SVN looks more painful compared to Git. The creation of a branch takes a lot of time because a copy of all the project must be made.

One of the advantages working with SVN must be linked to security. Having just one official copy of the project in a secure location can guarantee a reasonable degree of security in terms of hacking.

To have a more contrasting view and understanding of the two most used version control system, the next chapter will try to implement the class assignment using SVN as alternative workflow.

***

## 3. Implementation of the Alternative

As the location of the centralized copy of the project, I used one of ISEP's virtual servers made available during the first semester in the context of the SCOMRED course.

The main source of information for this tutorial comes from [Melvyn Drag YouTube channel](https://www.youtube.com/watch?v=Y9enCuIhwY8) and <http://svnbook.red-bean.com/>.

### 3.1 Install SVN on the server and client side

The server runs a Linux distribution. To install SVN we can run the following command:

```console
❯ sudo apt-get install subversion
```

On the client side, the OS is MacOS. To get SVN we can run the following command using Homebrew as a package manager:

```console
❯ brew install subversion
```

### 3.2 Set a new repository on the server

This will be the centralized part. The server will be the only machine with the totally of the project and it's history.

First create a new directory for the project.

```console
❯ mkdir -p /svn/repositories
```

After that we need to create a new repository by running the following command:

```console
❯ svnadmin create /svn/repositories/tut-basic
```

Before going further we must change the repository permissions by editing the configuration file. This file was created automatically when we run the previous command. The editing can done by any text editor. Here we will be using VIM.

```console
❯ vi /svn/respositories/tut-basic/conf/svnserve.conf
```

Uncomment the following lines:

```console
...
anon-access = read 
auth-access = write 
...
password-db = passwd
```

The first line enable anonymous users to read, the second line gives writing permission to authorized users. The third line defines where the users passwords are stored.

Lets now create a new user:

```console
❯ vi /svn/respositories/tut-basic/conf/passwd
```

Add a new user by specifying the username and a password.

```console
...
[users]
username = password
```

### 3.3 Start the server

```console
❯ svnserve -d -r /svn/repositories
```

The _-d_ flag is for daemon and the _-r_ flag to define the root directory.

### 3.4 Import the project, that is located in the client machine, to the server

Having a running server, we can import to it our project.

```console
svn import ./tut-basic svn://vs165.dei.isep.ipp.pt/tut-basic -m "Initial commit, import project to server"
```

The _svn import_ command has as first parameter the directory of the project that you want to import. In this case is _./tut-basic_.

The second parameter is the IP address of the server. In the previous chapter we defined the root directory. By doing that we just need the identify the folder in the root were we want to put the project, in this case is in a folder with the name _tut-basic_.

The _-m_ flag give us the possibility to define the commit message for this import.

We can now delete our project on the client side. The project is now stored on the server.

### 3.5 Check out a working copy from the repository

To get a copy of the project on the client side we can use the following command:

```console
❯ svn checkout svn://vs165.dei.isep.ipp.pt/tut-basic
```

### 3.6 How an SVN repository is normally structured

Usually there are three main folder in a project, _trunk_, _tags_ and _branches_. The _trunk_ folder stores a stable version of the project. The _tags_ folder stores a copy ("snapshot") of the entire project. The _branches_ folder is where we find the project under construction.

The names have the same meaning as in Git, but instead of _trunk_, Git has _master_

### 3.7 Tag the stable version that is in the trunk

```console
❯ svn copy svn://vs165.dei.isep.ipp.pt/tut-basic/trunk svn://vs165.dei.isep.ipp.pt/tut-basic/tags/v1.2.0 -m "New tag v1.2.0"
```

The _copy_ command creates a "snapshot" of the stable version in the _trunk_ folder to the _tags_ folder. The v1.2.0 is the folder name that will store the project. The command lets SVN keep track of the history of these files more efficiently.

### 3.8 Create a new branch to add new feature

To create a branch we also use the _copy_ command. The difference is where we now store this copy.

```console
❯ svn copy svn://vs165.dei.isep.ipp.pt/tut-basic/trunk svn://vs165.dei.isep.ipp.pt/tut-basic/branches/email-field -m "New branch for new email field"
```

The branch is now called _email-field_

If we go to the _branches_ directory on the client machine we will find it empty because the copy was made in the repository.

To get this copy we run the following command:

```console
❯ svn update
```

The command must be used inside the folder that we want to update; and it is recursive.

### 3.9 Implement new feature: test, implementation and debugging

Same as in 1.5

### 3.10 Add changes for next commit

To see the changes that you did on the project use the following command:

```console
❯ svn status
M       Employee.java
```

This shows that the Employee.java file was changed.
To commit the changes use the _commit_ command.

```console
❯ svn commit Employee.java -m "Add new attribute to the class Employee"
Sending        Employee.java
Transmitting file data .done
Committing transaction...
Committed revision 4.
```

If you create a new file that isn't yet under version control use the _add_ command.

```console
svn add <file name>
```

### 3.11 Merge branch with trunk

First we need to merge the trunk into my branch. Doing this allows to handle merge conflict in a more controlled way. This conflicts can occur if somebody on your team already made a merge to the trunk that you where not aware of.

```console
❯ svn merge ^/trunk
--- Recording mergeinfo for merge of r3 through r4 into '.':
 U   .

❯ svn commit -m "Merge trunk into email-field branch"
```

The _^/_ is an alias for the root directory.

We can now merge the _email-field_ branch with the _trunk_.

First change to the trunk directory, and then run the following command:

```console
❯ svn merge --reintegrate ^/branches/email-field
--- Merging differences between repository URLs into '.':
U    src/main/java/com/greglturnquist/payroll/Employee.java
--- Recording mergeinfo for merge between repository URLs into '.':
 U   .
```

The _--reintegrate_ flag is used to bring changes from a feature branch back into the feature branch's immediate ancestor branch. [_source_](http://svnbook.red-bean.com/en/1.7/svn.ref.svn.c.merge.html)

### 3.12 Continue class assignment

The next steps of the class assignment can be completed using the tools already described on this chapter.

***

THE END
