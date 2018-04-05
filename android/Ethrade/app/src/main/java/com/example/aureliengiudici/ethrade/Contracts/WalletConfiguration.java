package com.example.aureliengiudici.ethrade.Contracts;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;


public class WalletConfiguration implements Serializable {
    static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    private static final BigInteger ACCOUNT_UNLOCK_DURATION = BigInteger.valueOf(60);
    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    private static final String TAG = "WalletConfiguration";
    private String address;
    private String password;
    private Credentials myWallet;
    private WalletFile myWalleFile;
    private Admin web3j;
    private Boolean isConnect;
    private Context mcontext;

    public WalletConfiguration(Context context){
        try {
            this.mcontext = context;
            isConnect = false;
            this.web3jBuild();
        } catch (Exception e) {
           Log.e(TAG, e.toString());
        }
    }




    public void start(String password, File dir){
        try {
            this.password = password;
            if (!this.getWallet(password, dir)) {
                this.createWallet();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void start(String password){
        try {
            this.password = password;
            this.createWallet();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    private void web3jBuild() throws Exception {
            Log.i("Web3jBuild:",  "Il faut que ça marche");
            this.web3j  =  AdminFactory.build(new HttpService("http://192.168.0.16:8545"));
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            isConnect = true;
            Log.i("Connected:",  "to Ethereum client version:" + clientVersion);
    }
    private void createWallet() throws Exception {
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            myWallet = Credentials.create(ecKeyPair);
            this.myWalleFile = Wallet.createLight(password, ecKeyPair);
            System.out.println("address " + myWalleFile.getAddress());
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            String jsonStr = objectMapper.writeValueAsString(myWalleFile);
            System.out.println("keystore json file " + jsonStr);
            address = myWallet.getAddress();
            writeFile(jsonStr);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void writeFile(String json) {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, "account.json");
            Log.d(TAG, "file + " + gpxfile.getPath());
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(json);
            writer.flush();
            writer.close();

        }catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }

    public Boolean getWallet(String password, File dir) throws Exception {
        this.myWallet = WalletUtils.loadCredentials(password, dir);
        this.address = this.myWallet.getAddress();
        Log.d(TAG, this.address);
        //a tester
        getBalance(address);
        //sendEther("0x634c754432511a3142d2c7ba3667fe255d1bdd89",  "0xf06c998d943b1cc355cff0d3a0b0719e68d7cfdd", BigInteger.valueOf(1), "ertERT117711");
        return this.address != null;
    }

    public BigInteger getBalance(String address)
    {
    /*Web3j web3j  */

        if (!isConnect()) return null;
        EthGetBalance ethGetBalance = null;
        try {
            ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            Log.d(TAG, e.toString());
        } catch (ExecutionException e) {
            Log.d(TAG, e.toString());
        }
        Log.d(TAG, ethGetBalance.getBalance().toString());
        return ethGetBalance.getBalance();
    }

    public  String getAddress() {
        return address;
    }

    public Boolean isConnect() {
        return isConnect;
    }


    private boolean unlockAccount(String coinbaseAccountAddress, String coinbaseAccountPassword) throws Exception {
        PersonalUnlockAccount personalUnlockAccount =
                web3j.personalUnlockAccount(coinbaseAccountAddress, coinbaseAccountPassword, ACCOUNT_UNLOCK_DURATION).sendAsync().get();

        return personalUnlockAccount.accountUnlocked();
    }

    public Boolean sendEther(String addressToSend, String _address, BigInteger value, String _password) throws InterruptedException, ExecutionException, IOException, CipherException {
        Log.d(TAG, _address + " address | " + addressToSend + "  addressTO send | " + _password + " password|");
        try {
            getWallet(_password, new File(Environment.getExternalStorageDirectory().toString() + "/Download" + "/account.json"));
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
            return false;
        }
        Log.d(TAG, "Unlock");
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                _address, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        System.out.println(nonce);

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction (
                nonce, GAS_PRICE, GAS_LIMIT, addressToSend, value);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, this.myWallet);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();

        Log.i(TAG, "Transaction complete, view it at localHost" + transactionHash);
        return true;
    }

    public Web3j getWeb3j() {
        return this.web3j;
    }
}