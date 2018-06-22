Copy this jar to dropins


The server startup observer is an extension point that can be used to execute any custom code during server startup.
This allows a developer to perform any work before and/or after server startup (i.e. starting all the transports),
as required. The ServerStartupObserver interface needs to be implemented in order to use this extension and it includes
two methods to be overridden. The completingServerStartup() method could contain the implementation of any task that
needs to be performed before starting all the transports. The completedServerStartup() method could contain the
implementation of any task that needs to be performed after all the transports are completely started.


INFO - StartupHandlerActivator Activating Server Observer org.wso2.demo.ServerStartupHandler
....
INFO - ServerManager Server ready for processing...
INFO - MediationStatisticsComponent Statistic Reporter is Disabled
INFO - MediationStatisticsComponent Can't register an observer for mediationStatisticsStore. If you have disabled StatisticsReporter, please enable it in the Carbon.xml
INFO - ServerStartupHandler ***** Executing completingServerStartup *********
INFO - JMSListener JMS listener started
INFO - JMSListener Connection attempt: 1 for JMS Provider for service: JMSProxy was successful!
INFO - ServiceTaskManager Task manager for service : JMSProxy [re-]initialized
INFO - JMSListener Started to listen on destination : JMSProxy of type queue for service JMSProxy
INFO - PassThroughHttpListener Starting Pass-through HTTP Listener...
INFO - PassThroughListeningIOReactorManager Pass-through HTTP Listener started on 0.0.0.0:8280
INFO - PassThroughHttpSSLListener Starting Pass-through HTTPS Listener...
INFO - PassThroughListeningIOReactorManager Pass-through HTTPS Listener started on 0.0.0.0:8243
INFO - NioSelectorPool Using a shared selector for servlet write/read
INFO - NioSelectorPool Using a shared selector for servlet write/read
INFO - TaskServiceImpl Task service starting in STANDALONE mode...
INFO - NTaskTaskManager Initialized task manager. Tenant [-1234]
INFO - NTaskTaskManager Scheduled task [NTask::-1234::MyTask]
INFO - RegistryEventingServiceComponent Successfully Initialized Eventing on Registry
INFO - JMXServerManager JMX Service URL  : service:jmx:rmi://localhost:11111/jndi/rmi://localhost:9999/jmxrmi
INFO - ServerStartupHandler ***** Executing completedServerStartup *********