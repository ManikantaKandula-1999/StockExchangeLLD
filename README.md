# In-Memory Trading System (Stock Exchange Simulation)

Design and implement an **efficient in-memory trading system** similar to a stock exchange, where registered users can **place, execute, and cancel trades**. The system should demonstrate **synchronization and concurrency** in a **multi-threaded environment**.

---

# Functional Requirements

The system should support the following functionalities:

### 1. Order Management

* A registered user can:

    * **Place orders**
    * **Modify orders**
    * **Cancel orders**

### 2. Order Status

* A user should be able to **query the status of their order**.

### 3. Trade Execution **[IMPORTANT]**

* The system must execute trades based on **matching buy and sell orders**.
* A trade is executed when:

    * The **buy price equals the sell price**.
* If **multiple eligible orders** exist with the same price:

    * The **oldest orders must be matched first**.

### 4. Concurrency Handling **[IMPORTANT]**

* The system must correctly handle **concurrent operations**, including:

    * Order placement
    * Order modification
    * Order cancellation
    * Trade execution

### 5. Order Book

* Maintain an **order book per stock symbol**.
* Each order book holds all **currently unexecuted orders**.

---

# Data Storage Requirements

The system must store the following entities and attributes.

## 1. User Details

* **User ID**
* **User Name**
* **Phone Number**
* **Email ID**

---

## 2. Orders

* **Order ID**
* **User ID**
* **Order Type** (Buy / Sell)
* **Stock Symbol** (e.g., RELIANCE, WIPRO)
* **Quantity**
* **Price**
* **Order Accepted Timestamp**
* **Status**

    * ACCEPTED
    * REJECTED
    * CANCELED

---

## 3. Trades

* **Trade ID**
* **Trade Type** (Buy / Sell)
* **Buyer Order ID**
* **Seller Order ID**
* **Stock Symbol**
* **Quantity**
* **Price**
* **Trade Timestamp**

---

# Additional Functionality (Optional)

If time permits, implement **trade expiry**:

* A trade should be **automatically canceled** if it is **not executed within a specific time window**.

Implementing this feature will **earn extra credit**.

---

# Expectations

Your implementation should meet the following expectations:

* The code should be **executable** (partial running code is acceptable if necessary).
* Code should be **clean, well-structured, and properly refactored**.
* **Exceptions must be handled gracefully**.
* All attributes defined in the **Data Storage Requirements** section must be stored.
* All functionalities listed in the **Functional Requirements** section must be implemented.

If additional time is available, implement the **optional functionality** for extra credit.

---

# Guidelines

* **User registration is not required**.
* Assume that **some dummy users are already registered**, and use them in the system.
* Use **in-memory data structures** of your preferred programming language to store the data.
* The design should include **proper abstractions** so that **persistent storage (like databases)** can be easily integrated later.

