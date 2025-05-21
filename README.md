# DWS Code Challenge
Add functionality for a transfer of money between accounts

# Features
- Transfers should be specified by providing: (a) accountFromId (b) accountToId (c) amount to transfer between accounts
- Transfer amount be a positive number
- FromAccount cannot end up with negative balance
- NotificationService interface: Do NOT provide implementation for this service
- Should be thread-safe manner

# Tech Stack Used
- Java 17
- Spring Boot 3.x
- Maven
- jakarta.validations.x

# API Endpoint
/v1/accounts/fundsTransfer

# Request Body
{
  "fromAccountId":"",
  "toAccountId":"",
  "transferAmount":
}
