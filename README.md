readhead-support-reproducer
========================
Author: Heike Winkelvoß

Based on the generated JavaEE Web Project in Eclipse by Pete Muir Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

This project reproduces an IllegalArgumentException thrown by Hibernate when creating a TypedQuery in connection with managed executor services and CDI-Beans (not EJBs), see
<https://access.redhat.com/support/cases/#/case/02444670>


The project uses the original sources with some changes in the originally generated pom.xml, the persistence.xml and the reproducer method for EntityManager in Resources.

Both the dependencies and the production of an EntityManager are the ones used by our own project.

Additionally the following managed executor service ist required

	/subsystem=ee/managed-executor-service=batchMainThreadExecutorService:add(jndi-name=java:jboss/ee/concurrency/executor/batchMainThreadExecutorService, core-threads=1, thread-factory=default, keepalive-time=60000, hung-task-threshold=0, reject-policy=ABORT, long-running-tasks=true, queue-length=0)

As data source we use java:jboss/datasources/ExampleDS as generated.

Workflow
-----------

Starting point is CDIListener. It is application scoped an gets both an executor service injected as resource and an application scoped instance of the Callable PollMembersTask injected.

PollMembersTask starts an infinite loop loading all members via a TypedQuery every 2 seconds.

CDILIstener defines a method observing the application scope. Once application scope is ready PollMemberTask is submitted to the ManagedExecutorService and the above mentioned loop starts loading all members frequently.

How to reproduce the error
-----------

Start JBoss and deploy readhead-support-reproducer.war.

Keep JBoss running and undeploy readhead-support-reproducer.war. Deploy readhead-support-reproducer.war

After redeployment of readhead-support-reproducer.war creating the TypedQuery in MemberRepository.loadMembers() throws an IllegalArgumentException:

[de.hessen.hzd.data.MemberRepository] (EE-ManagedThreadFactory-default-Thread-1) exception when loading the members: Type specified for TypedQuery [de.hessen.hzd.model.Member] is incompatible with query return type [class de.hessen.hzd.model.Member]: java.lang.IllegalArgumentException: Type specified for TypedQuery [de.hessen.hzd.model.Member] is incompatible with query return type [class de.hessen.hzd.model.Member]


(Attention: Sometimes PollMembersTask does not start running. In this case repeat undeploying and deploying one time and you should recognize the IllegalArgumentException)


How to recover from the error without work arround
-----------

Restart JBoss.


How to work arround the error
-----------

In MemberRepository.loadMembers() replace TypedQuery by Query as commented there.
