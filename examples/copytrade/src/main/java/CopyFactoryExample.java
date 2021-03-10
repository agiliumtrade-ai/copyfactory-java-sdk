import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cloud.metaapi.sdk.clients.copy_factory.ConfigurationClient;
import cloud.metaapi.sdk.clients.copy_factory.models.CopyFactoryAccountUpdate;
import cloud.metaapi.sdk.clients.copy_factory.models.CopyFactoryStrategySubscription;
import cloud.metaapi.sdk.clients.copy_factory.models.CopyFactoryStrategyUpdate;
import cloud.metaapi.sdk.clients.copy_factory.models.StrategyId;
import cloud.metaapi.sdk.copy_factory.CopyFactory;
import cloud.metaapi.sdk.meta_api.MetaApi;
import cloud.metaapi.sdk.meta_api.MetatraderAccount;

public class CopyFactoryExample {

  // your MetaApi API token
  private static String token = getEnvOrDefault("TOKEN", "<put in your token here>");
  // your master MetaApi account id
  private static String masterAccountId = getEnvOrDefault("MASTER_ACCOUNT_ID", "<put in your MT login here>");
  // your slave MetaApi account id
  private static String slaveAccountId = getEnvOrDefault("SLAVE_ACCOUNT_ID", "<put in your MT login here>");
  
  public static void main(String[] args) {
    try {
      MetaApi api = new MetaApi(token);
      CopyFactory copyFactory = new CopyFactory(token);
      List<MetatraderAccount> accounts = api.getMetatraderAccountApi().getAccounts().join();
      MetatraderAccount masterMetaapiAccount = accounts.stream()
        .filter(a -> a.getId().equals(masterAccountId)).findFirst().get();
      if (!masterMetaapiAccount.getApplication().equals("CopyFactory")) {
        throw new Exception("Please specify CopyFactory application field value in your MetaApi account in "
            + "order to use it in CopyFactory API");
      }
      
      MetatraderAccount slaveMetaapiAccount = accounts.stream()
        .filter(a -> a.getId().equals(slaveAccountId)).findFirst().get();
      if (!slaveMetaapiAccount.getApplication().equals("CopyFactory")) {
        throw new Exception("Please specify CopyFactory application field value in your MetaApi account in "
            + "order to use it in CopyFactory API");
      }
      
      ConfigurationClient configurationApi = copyFactory.getConfigurationApi();
      String masterAccountId = configurationApi.generateAccountId();
      String slaveAccountId = configurationApi.generateAccountId();
      configurationApi.updateAccount(masterAccountId, new CopyFactoryAccountUpdate() {{
        name = "Demo master account";
        connectionId = masterMetaapiAccount.getId();
        subscriptions = new ArrayList<>();
      }}).join();
      
      // create a strategy being copied
      StrategyId strategyId = configurationApi.generateStrategyId().join();
      configurationApi.updateStrategy(strategyId.id, new CopyFactoryStrategyUpdate() {{
        name = "Test strategy";
        description = "Some useful description about your strategy";
        connectionId = masterMetaapiAccount.getId();
      }}).join();
      
      // subscribe slave CopyFactory accounts to the strategy
      String sid = strategyId.id;
      configurationApi.updateAccount(slaveAccountId, new CopyFactoryAccountUpdate() {{
        name = "Demo slave account";
        connectionId = slaveMetaapiAccount.getId();
        subscriptions = Arrays.asList(new CopyFactoryStrategySubscription() {{
          strategyId = sid;
          multiplier = 1.0;
        }});
      }}).join();
      
      System.out.println("Please note that it can take some time for CopyFactory to initialize accounts. "
        + "During this time the MetaApi accounts may redeploy a couple of times. After initialization "
        + "finishes, you can copy trades from your master to slave account.");
    } catch (Exception err) {
      err.printStackTrace();
    }
    System.exit(0);
  }
  
  private static String getEnvOrDefault(String name, String defaultValue) {
    String result = System.getenv(name);
    return (result != null ? result : defaultValue);
  }
}