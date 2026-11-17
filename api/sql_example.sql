CREATE TABLE accounts (
    id BIGINT PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    balance NUMERIC(18,2) NOT NULL CHECK (balance >= 0)
);

CREATE TABLE transfers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    source_account_id BIGINT NOT NULL,
    destination_account_id BIGINT NOT NULL,
    amount NUMERIC(18,2) NOT NULL CHECK (amount > 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_source FOREIGN KEY (source_account_id)
        REFERENCES accounts(id),
    CONSTRAINT fk_destination FOREIGN KEY (destination_account_id)
        REFERENCES accounts(id),
    CONSTRAINT chk_different_accounts
        CHECK (source_account_id <> destination_account_id)
);

-- SEEDS
INSERT INTO accounts (id, holder_name, balance) VALUES
    (1001, 'Juan Pérez', 5000.00),
    (1002, 'María López', 2000.00),
    (1003, 'Carlos Gómez', 10000.00);

-- Indexes on foreign keys for fast transfer history lookups (PostgreSQL does not auto-index FKs)
CREATE INDEX idx_transfers_source_account ON transfers(source_account_id);
CREATE INDEX idx_transfers_destination_account ON transfers(destination_account_id);

-- Index on created_at for chronological sorting of transaction history
CREATE INDEX idx_transfers_created_at ON transfers(created_at DESC);