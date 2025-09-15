-- Add created_by_id column to sub_task table
ALTER TABLE sub_task ADD COLUMN created_by_id BIGINT;

-- Add foreign key constraint
ALTER TABLE sub_task ADD CONSTRAINT fk_subtask_created_by 
    FOREIGN KEY (created_by_id) REFERENCES users(id);

-- Update existing records to set created_by_id to the project manager
-- This is a reasonable default for existing data
UPDATE sub_task 
SET created_by_id = (
    SELECT p.manager_id 
    FROM project p 
    WHERE p.id = sub_task.project_id
);

-- Make the column NOT NULL after setting default values
ALTER TABLE sub_task ALTER COLUMN created_by_id SET NOT NULL;