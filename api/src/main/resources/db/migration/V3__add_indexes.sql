-- ./api/src/main/resources/db/migration/V3__add_indexes.sql

-- Indexes on foreign keys for fast transfer history lookups (PostgreSQL does not auto-index FKs)
CREATE INDEX idx_transfers_source_account ON transfers(source_account_id);
CREATE INDEX idx_transfers_destination_account ON transfers(destination_account_id);

-- Index on created_at for chronological sorting of transaction history
CREATE INDEX idx_transfers_created_at ON transfers(created_at DESC);