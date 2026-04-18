ALTER TABLE tea DROP CONSTRAINT tea_vendor_id_fkey;
ALTER TABLE tea ADD CONSTRAINT tea_vendor_id_fkey
    foreign key (vendor_id) REFERENCES  vendor(id) ON DELETE SET NULL;