CREATE TABLE chat_sessions (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               chat_name VARCHAR(255) NOT NULL,
                               user_name VARCHAR(255) NOT NULL,
                               chat_history JSONB NOT NULL DEFAULT '[]'::jsonb,
                               created_by VARCHAR(255) NOT NULL,
                               last_access TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               status VARCHAR(50) DEFAULT 'active'
);

-- (Optional but Recommended) Indexes for better query performance

-- Index for quickly finding chats by a specific user
CREATE INDEX idx_chat_sessions_user_name ON chat_sessions(user_name);

-- Index for sorting or filtering by the most recently accessed chats
CREATE INDEX idx_chat_sessions_last_access ON chat_sessions(last_access);

-- GIN Index for efficiently querying inside the JSONB chat_history
-- (e.g., searching for specific messages or metadata inside the JSON array)
CREATE INDEX idx_chat_sessions_chat_history ON chat_sessions USING GIN (chat_history);