-- This script should be run AFTER the application starts and creates the new created_by_id column
-- It updates existing sub-tasks to set the created_by_id to the project manager
-- This is a reasonable default for existing data

UPDATE sub_task 
SET created_by_id = (
    SELECT p.manager_id 
    FROM project p 
    WHERE p.id = sub_task.project_id
)
WHERE created_by_id IS NULL;