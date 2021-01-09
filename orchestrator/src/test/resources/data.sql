insert into tbl_users (
  email, password, firstName, lastName, enabled, locked, userRole
)
values
  ('confirmed@user.com', 'pass', 'first', 'last', 1, 0, 0),
  ('not@confirmed.com', 'pass', 'first', 'last', 0, 0, 0);