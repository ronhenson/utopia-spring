insert into tbl_users (
  email, password, firstName, lastName, enabled, locked, userRole
)
values
  ('not@confirmed.com', 'pass', 'first', 'last', 0, 0, 0);