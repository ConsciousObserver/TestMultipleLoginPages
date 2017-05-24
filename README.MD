A sample project with multiple login page in using Spring Security.

admin resources are under /admin/** (login with admin/test)
Everything else is user /** (login with test/test)

### Note

	Since these login pages are not from different applications, they share the SecurityContextHolder or the security context. So if you login from one login page and then try to go the protected resource of the other, you won't be redirected to the next login page. Instead you'll get the 403 (depending on the roles assigned by the different login pages). At a time only one login session can be maintained.