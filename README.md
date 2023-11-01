# arseedingsdk4j
Arseeding Java SDK  (Arweave Java SDK)
arseedingsdk4j is an arseeding java sdk and supports Arweavede's bundle (ANS-104) transaction type. Payments are made through everPay and the data is eventually uploaded to the Arweave blockchain network. If you have any questions during use, please contact us

# Architecture
![img.png](img.png)

# Features
* send data
* pay the order
* send and pay
* send data using API keys
* upload folder data
* get bundle fees
* get user orders

# getting-started
### maven-dependency
```agsl
<dependency>
    <groupId>com.github.permadao</groupId>
    <artifactId>arseedingsdk4j-sdk</artifactId>
    <version>${latest-version}</version>
</dependency>
```
### wallet init
EthereumWallet
```agsl
String fileName = "your file";
EthereumWallet ethereumWallet1 =
        EthereumWallet.loadEthereumWallet("your password", new File(fileName));
        
EthereumWallet ethereumWallet2 =
        EthereumWallet.loadEthereumWallet("your private key"); 
```
ArweaveWallet
```agsl
ArweaveWallet arweaveWallet = 
        ArweaveWallet.loanArweaveWallet("your private key");
```

### send data
```agsl

```