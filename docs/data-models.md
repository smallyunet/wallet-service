# Data Models

This document describes the key data models used in the Wallet Server API.

## Table of Contents

- [Response Models](#response-models)
  - [BalanceResponse](#balanceresponse)
  - [EthTransferResponse](#ethtransferresponse)
  - [TransactionStatusResponse](#transactionstatusresponse)
  - [GasFeeSuggestion](#gasfeesuggestion)
  - [NonceResponse](#nonceresponse)
  - [BlockscoutTransactionResponse](#blockscout-transaction-response)
- [Request Models](#request-models)
  - [EthTransferRequest](#ethtransferrequest)

## Response Models

### BalanceResponse

Represents a balance query response for ETH or BTC.

**Package**: `com.example.wallet.domain.BalanceResponse`

**Fields**:
| Name | Type | Description |
|------|------|-------------|
| coin | String | The cryptocurrency code (e.g., "ETH", "BTC") |
| network | String | The network name (e.g., "mainnet", "sepolia") |
| address | String | The wallet address |
| balance | String | The account balance in the smallest unit (wei for ETH, satoshi for BTC), in hex format |

**Example JSON**:
```json
{
  "coin": "ETH",
  "network": "sepolia",
  "address": "0x1234567890abcdef1234567890abcdef12345678",
  "balance": "0x1bc16d674ec80000"
}
```

### EthTransferResponse

Represents the response from an ETH transaction broadcast.

**Package**: `com.example.wallet.domain.eth.EthTransferResponse`

**Fields**:
| Name | Type | Description |
|------|------|-------------|
| transaction_hash | String | The hash of the transaction |
| from | String | The sender's address |
| to | String | The recipient's address |
| value | String | The amount transferred in wei (hex format) |
| block_hash | String | The hash of the block containing the transaction |
| block_number | String | The block number |
| gas_used | String | The amount of gas used |
| status | String | The status of the transaction ("pending", "confirmed", or "failed") |
| error | String | Error message, if any |

**Example JSON**:
```json
{
  "transaction_hash": "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
  "from": "0x1234567890abcdef1234567890abcdef12345678",
  "to": "0xabcdef1234567890abcdef1234567890abcdef12",
  "value": "0x1bc16d674ec80000",
  "block_hash": "0xabcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890",
  "block_number": "12345678",
  "gas_used": "21000",
  "status": "confirmed"
}
```

### TransactionStatusResponse

Represents detailed information about a transaction's status and receipt.

**Package**: `com.example.wallet.domain.eth.TransactionStatusResponse`

**Fields**:
| Name | Type | Description |
|------|------|-------------|
| transaction_hash | String | The hash of the transaction |
| block_hash | String | The hash of the block containing the transaction |
| block_number | String | The block number |
| from | String | The sender's address |
| to | String | The recipient's address |
| gas_used | String | The amount of gas used |
| gas_price | String | The gas price in wei |
| effective_gas_price | String | The effective gas price (post EIP-1559) |
| value | String | The amount transferred in wei |
| status | String | The status of the transaction ("success", "failed", "pending", "not_found", or "error") |
| cumulative_gas_used | String | The total gas used in the block up to this transaction |
| logs_bloom | String | The bloom filter for event logs |
| contract_address | String | The address of the created contract, if any |
| logs | Array | List of event logs emitted during transaction execution |
| confirmation_count | Integer | Number of confirmations (blocks mined after this transaction) |
| error | String | Error message, if any |

**Example JSON**:
```json
{
  "transaction_hash": "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
  "block_hash": "0xabcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890",
  "block_number": "12345678",
  "from": "0x1234567890abcdef1234567890abcdef12345678",
  "to": "0xabcdef1234567890abcdef1234567890abcdef12",
  "gas_used": "21000",
  "gas_price": "50000000000",
  "effective_gas_price": "50000000000",
  "value": "1000000000000000000",
  "status": "success",
  "cumulative_gas_used": "12500000",
  "logs_bloom": "0x0000...",
  "logs": [
    {
      "address": "0x1234...",
      "topics": ["0xabcd..."],
      "data": "0x0000...",
      "log_index": "0",
      "block_number": "12345678",
      "block_hash": "0xabcd...",
      "transaction_index": "0"
    }
  ],
  "confirmation_count": 10
}
```

### GasFeeSuggestion

Represents gas fee suggestions for different transaction confirmation times.

**Package**: `com.example.wallet.domain.eth.GasFeeSuggestion`

**Fields**:
| Name | Type | Description |
|------|------|-------------|
| base_fee | String | The base fee per gas in gwei |
| unit | String | The unit of measurement (always "gwei") |
| slow | GasFeeDetail | Fee suggestion for slow confirmation (~2 minutes) |
| average | GasFeeDetail | Fee suggestion for average confirmation (~1 minute) |
| fast | GasFeeDetail | Fee suggestion for fast confirmation (~30 seconds) |
| fastest | GasFeeDetail | Fee suggestion for fastest confirmation (~15 seconds) |

**GasFeeDetail Fields**:
| Name | Type | Description |
|------|------|-------------|
| max_fee | String | The maximum fee per gas in gwei |
| max_priority_fee | String | The maximum priority fee per gas in gwei |
| estimated_seconds | Integer | The estimated time to confirmation in seconds |

**Example JSON**:
```json
{
  "base_fee": "25.45",
  "unit": "gwei",
  "slow": {
    "max_fee": "25.45",
    "max_priority_fee": "2.55",
    "estimated_seconds": 120
  },
  "average": {
    "max_fee": "30.54",
    "max_priority_fee": "5.09",
    "estimated_seconds": 60
  },
  "fast": {
    "max_fee": "38.18",
    "max_priority_fee": "7.64",
    "estimated_seconds": 30
  },
  "fastest": {
    "max_fee": "50.90",
    "max_priority_fee": "12.73",
    "estimated_seconds": 15
  }
}
```

### NonceResponse

Represents the response for a nonce query.

**Package**: `com.example.wallet.domain.eth.NonceResponse`

**Fields**:
| Name | Type | Description |
|------|------|-------------|
| address | String | The Ethereum address |
| network | String | The network name |
| nonce | String | The nonce value in hex format |
| nonce_decimal | Long | The nonce value in decimal format |

**Example JSON**:
```json
{
  "address": "0x1234567890abcdef1234567890abcdef12345678",
  "network": "sepolia",
  "nonce": "0xa",
  "nonce_decimal": 10
}
```

### BlockscoutTransactionResponse

Represents transaction history data from Blockscout API.

**Package**: `com.example.wallet.domain.blockscout.BlockscoutTransactionResponse`

**Fields**:
| Name | Type | Description |
|------|------|-------------|
| items | Array | List of transaction objects |
| next_page_params | Object | Pagination parameters for the next page, or null if there are no more pages |

**Transaction Fields**:
| Name | Type | Description |
|------|------|-------------|
| hash | String | The transaction hash |
| block_number | Integer | The block number |
| from | AddressInfo | Information about the sender |
| to | AddressInfo | Information about the recipient |
| value | String | The amount transferred in wei |
| fee | Fee | The transaction fee information |
| timestamp | String | The transaction timestamp in ISO format |
| status | String | The transaction status ("ok", "error") |

**AddressInfo Fields**:
| Name | Type | Description |
|------|------|-------------|
| hash | String | The address hash |

**Fee Fields**:
| Name | Type | Description |
|------|------|-------------|
| type | String | The fee type (usually "gas_fee") |
| value | String | The fee value in wei |

**Example JSON**:
```json
{
  "items": [
    {
      "hash": "0x1234...",
      "block_number": 123456,
      "from": {
        "hash": "0xabcd..."
      },
      "to": {
        "hash": "0xefgh..."
      },
      "value": "1000000000000000000",
      "fee": {
        "type": "gas_fee",
        "value": "21000"
      },
      "timestamp": "2025-08-19T12:34:56Z",
      "status": "ok"
    }
  ],
  "next_page_params": null
}
```

## Request Models

### EthTransferRequest

Represents a request to broadcast a signed Ethereum transaction.

**Package**: `com.example.wallet.domain.eth.EthTransferRequest`

**Fields**:
| Name | Type | Description |
|------|------|-------------|
| signed_transaction | String | The signed transaction data as a hex string |
| from | String | The sender's address (optional, for information only) |
| to | String | The recipient's address (optional, for information only) |
| value | String | The amount transferred in wei (optional, for information only) |

**Example JSON**:
```json
{
  "signed_transaction": "0xf86c0a8502540be40082520894381e247bef0ebc21284f96d31b64c95261e69176880de0b6b3a7640000801ca00e14cbf3c3626db5c75d0ef9fb3d226271e12a897ea1725168b95cb37edb96dda07795a77daa8912b4fe4cb38089aee6539c5f24ed5f7f3afd68155c717686688d",
  "from": "0x1234567890abcdef1234567890abcdef12345678",
  "to": "0xabcdef1234567890abcdef1234567890abcdef12",
  "value": "0x1bc16d674ec80000"
}
```

## Configuration Models

### AppProperties

Configuration properties for the application.

**Package**: `com.example.wallet.config.AppProperties`

**Fields**:
| Name | Type | Description |
|------|------|-------------|
| env | String | The environment name |
| blockscout | Map | Blockscout API endpoints by network |
| rpc | RpcConfig | RPC endpoints configuration |
| security | SecurityConfig | Security configuration |

**RpcConfig Fields**:
| Name | Type | Description |
|------|------|-------------|
| eth | Map | Ethereum RPC endpoints by network |
| btc | Map | Bitcoin API endpoints by network |

**SecurityConfig Fields**:
| Name | Type | Description |
|------|------|-------------|
| allowOrigins | String | CORS allowed origins configuration |

**Example YAML**:
```yaml
app:
  env: "default"
  blockscout:
    eth:
      mainnet: "https://eth.blockscout.com/api/v2/addresses"
      sepolia: "https://eth-sepolia.blockscout.com/api/v2/addresses"
  rpc:
    eth:
      mainnet: "https://eth-mainnet.example.com"
      sepolia: "https://ethereum-sepolia-rpc.publicnode.com"
    btc:
      mainnet: "https://blockstream.info/api"
      testnet: "https://blockstream.info/testnet/api"
  security:
    allowOrigins: "*"
```
