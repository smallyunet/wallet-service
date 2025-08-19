package com.example.wallet.domain.blockscout;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a transaction from Blockscout API
 */
public class Transaction {
    @JsonProperty("priority_fee")
    private String priorityFee;
    
    @JsonProperty("raw_input")
    private String rawInput;
    
    private String result;
    private String hash;
    
    @JsonProperty("max_fee_per_gas")
    private String maxFeePerGas;
    
    @JsonProperty("revert_reason")
    private String revertReason;
    
    @JsonProperty("confirmation_duration")
    private List<Number> confirmationDuration;
    
    @JsonProperty("transaction_burnt_fee")
    private String transactionBurntFee;
    
    private Integer type;
    
    @JsonProperty("token_transfers_overflow")
    private Object tokenTransfersOverflow;
    
    private Integer confirmations;
    private Integer position;
    
    @JsonProperty("max_priority_fee_per_gas")
    private String maxPriorityFeePerGas;
    
    @JsonProperty("transaction_tag")
    private Object transactionTag;
    
    @JsonProperty("created_contract")
    private Object createdContract;
    
    private String value;
    private AddressInfo from;
    
    @JsonProperty("gas_used")
    private String gasUsed;
    
    private String status;
    private AddressInfo to;
    
    @JsonProperty("authorization_list")
    private List<Object> authorizationList;
    
    private String method;
    private Fee fee;
    private List<Object> actions;
    
    @JsonProperty("gas_limit")
    private String gasLimit;
    
    @JsonProperty("gas_price")
    private String gasPrice;
    
    @JsonProperty("decoded_input")
    private Object decodedInput;
    
    @JsonProperty("token_transfers")
    private Object tokenTransfers;
    
    @JsonProperty("base_fee_per_gas")
    private String baseFeePerGas;
    
    private String timestamp;
    private Integer nonce;
    
    @JsonProperty("historic_exchange_rate")
    private Object historicExchangeRate;
    
    @JsonProperty("transaction_types")
    private List<String> transactionTypes;
    
    @JsonProperty("exchange_rate")
    private Object exchangeRate;
    
    @JsonProperty("block_number")
    private Integer blockNumber;
    
    @JsonProperty("has_error_in_internal_transactions")
    private Boolean hasErrorInInternalTransactions;

    // Getters and Setters
    public String getPriorityFee() {
        return priorityFee;
    }

    public void setPriorityFee(String priorityFee) {
        this.priorityFee = priorityFee;
    }

    public String getRawInput() {
        return rawInput;
    }

    public void setRawInput(String rawInput) {
        this.rawInput = rawInput;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMaxFeePerGas() {
        return maxFeePerGas;
    }

    public void setMaxFeePerGas(String maxFeePerGas) {
        this.maxFeePerGas = maxFeePerGas;
    }

    public String getRevertReason() {
        return revertReason;
    }

    public void setRevertReason(String revertReason) {
        this.revertReason = revertReason;
    }

    public List<Number> getConfirmationDuration() {
        return confirmationDuration;
    }

    public void setConfirmationDuration(List<Number> confirmationDuration) {
        this.confirmationDuration = confirmationDuration;
    }

    public String getTransactionBurntFee() {
        return transactionBurntFee;
    }

    public void setTransactionBurntFee(String transactionBurntFee) {
        this.transactionBurntFee = transactionBurntFee;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getTokenTransfersOverflow() {
        return tokenTransfersOverflow;
    }

    public void setTokenTransfersOverflow(Object tokenTransfersOverflow) {
        this.tokenTransfersOverflow = tokenTransfersOverflow;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getMaxPriorityFeePerGas() {
        return maxPriorityFeePerGas;
    }

    public void setMaxPriorityFeePerGas(String maxPriorityFeePerGas) {
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
    }

    public Object getTransactionTag() {
        return transactionTag;
    }

    public void setTransactionTag(Object transactionTag) {
        this.transactionTag = transactionTag;
    }

    public Object getCreatedContract() {
        return createdContract;
    }

    public void setCreatedContract(Object createdContract) {
        this.createdContract = createdContract;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AddressInfo getFrom() {
        return from;
    }

    public void setFrom(AddressInfo from) {
        this.from = from;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AddressInfo getTo() {
        return to;
    }

    public void setTo(AddressInfo to) {
        this.to = to;
    }

    public List<Object> getAuthorizationList() {
        return authorizationList;
    }

    public void setAuthorizationList(List<Object> authorizationList) {
        this.authorizationList = authorizationList;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public List<Object> getActions() {
        return actions;
    }

    public void setActions(List<Object> actions) {
        this.actions = actions;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Object getDecodedInput() {
        return decodedInput;
    }

    public void setDecodedInput(Object decodedInput) {
        this.decodedInput = decodedInput;
    }

    public Object getTokenTransfers() {
        return tokenTransfers;
    }

    public void setTokenTransfers(Object tokenTransfers) {
        this.tokenTransfers = tokenTransfers;
    }

    public String getBaseFeePerGas() {
        return baseFeePerGas;
    }

    public void setBaseFeePerGas(String baseFeePerGas) {
        this.baseFeePerGas = baseFeePerGas;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public Object getHistoricExchangeRate() {
        return historicExchangeRate;
    }

    public void setHistoricExchangeRate(Object historicExchangeRate) {
        this.historicExchangeRate = historicExchangeRate;
    }

    public List<String> getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(List<String> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public Object getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Object exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Boolean getHasErrorInInternalTransactions() {
        return hasErrorInInternalTransactions;
    }

    public void setHasErrorInInternalTransactions(Boolean hasErrorInInternalTransactions) {
        this.hasErrorInInternalTransactions = hasErrorInInternalTransactions;
    }
}
