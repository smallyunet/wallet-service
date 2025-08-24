# ERC-20 Token Transfer Example

## Using Cast Tool for ERC-20 Token Transfers

### 1. Check Token Balance
```bash
# Check token balance
cast call [token contract address] "balanceOf(address)(uint256)" [your wallet address] \
  --rpc-url "https://ethereum-sepolia-rpc.publicnode.com"
```

### 2. Create Signed Transaction Data (without broadcasting)
```bash
# Create signed transaction without broadcasting
cast send \
  --rpc-url "https://ethereum-sepolia-rpc.publicnode.com" \
  --private-key "your private key" \
  --chain 11155111 \
  [token contract address] \
  "transfer(address,uint256)" \
  [recipient address] \
  [transfer amount, e.g. 1000000000000000000] \
  --gas-price 25gwei \
  --priority-gas-price 1gwei \
  --json \
  --create
```

### 3. Submit Transaction Data to the Server
```bash
# Use the signed transaction data generated in the previous step
curl -X 'POST' \
  'http://localhost:8080/v1/eth/sepolia/token-transfer' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "signed_transaction": "0xgenerated signed transaction data (long hex string)",
  "from": "your wallet address",
  "to": "recipient address",
  "token_address": "token contract address",
  "value": "transfer amount",
  "token_symbol": "token symbol like USDC"
}'
```

## Important Notes

1. **Distinguish between transaction hash and signed transaction data**
   - Transaction hash: 32 bytes long, e.g. `0x4cf5c3ae1357c3cd4725692ccd6d76e62522ab4cc8b79cc1010a3dbfe877c6bc`
   - Signed transaction data: longer hex string containing complete transaction information

2. **Token Decimals**
   - Most ERC-20 tokens use 18 decimal places
   - For example, to send 1 token, specify 1000000000000000000 (1 followed by 18 zeros)

3. **Gas Limits**
   - ERC-20 token transfers require more gas than regular ETH transfers
   - Recommend using at least 100,000 gas limit (cast will auto-estimate by default)

4. **Verify Token Contract**
   - Ensure the token contract supports the standard ERC-20 interface
   - Use the `transfer(address,uint256)` method for transfers

## Example Flow

1. Sign the token transfer transaction using a private key on the client
2. Send the signed transaction data (not the transaction hash) to the server
3. Server broadcasts the signed transaction to the blockchain network
4. Server returns the transaction hash and status information
