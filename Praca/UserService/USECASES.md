# User-Facing Use Cases

## Authentication & Account

| Use Case | Route | Required Role |
|----------|-------|---------------|
| User Login | `/login` | None (public) |
| User Registration | `/register/:inviteCode` | None (public, requires invite) |
| User Logout | Any | Authenticated |
| View Landing Page | `/` | None (public) |

## Voting Management

| Use Case | Route | Required Role |
|----------|-------|---------------|
| View Votings (All tab) | `/voting` | `votings:readAll` |
| View Votings (Owned tab) | `/voting` | `votings:manage` |
| View Votings (Available tab) | `/voting` | `votings:participate` |
| View Votings (Finished tab) | `/voting` | `votings:read` OR `votings:readAll` |
| Create Voting | `/voting/create` | `votings:manage` |
| Edit Voting | `/voting/edit/:id` | `votings:manage` (owner only) |
| View Voting Details | `/voting/:id` | `votings:read` or participant |
| Delete Voting | `/voting/:id` | `votings:manage` (owner only, before start) |
| Lock Voting | `/voting/:id` | `votings:manage` (owner only) |
| View Live Voting | `/voting/:id/live` | `votings:read` or participant |
| Start/End Live Voting | `/voting/:id/live` | Owner only |
| Cast Vote | `/voting/:id/live` | Eligible voter |
| View Results | `/voting/:id` | After voting ends |

## Account Administration

| Use Case | Route | Required Role |
|----------|-------|---------------|
| View All Accounts | `/accounts` | `accounts:read` |
| Generate Invite Code | `/accounts` | `accounts:invite` |
| View Account Details | `/accounts/:id` | `accounts:read` |
| Archive Account | `/accounts/:id` | `accounts:archive` |

## Group Management

| Use Case | Route | Required Role |
|----------|-------|---------------|
| View All Groups | `/groups` | `groups:read` |
| Create Group | `/groups` | `groups:create` |
| View Group Details | `/groups/:id` | `groups:read` |
| Manage Group Members | `/groups/:id` | Group manager |
| Delete Group | `/groups/:id` | Group manager |

## Role Administration

| Use Case | Route | Required Role |
|----------|-------|---------------|
| View All Roles | `/roles` | `role:read` |
| Create Role | `/roles` | `role:create` |
| Delete Role | `/roles` | `role:delete` |

## Estate Management

| Use Case | Route | Required Role |
|----------|-------|---------------|
| View Buildings | `/estate` | `estate:read` |
| CRUD Buildings/Floors/Flats | `/estate` | `estate:manage` |

## Notifications

| Use Case | Route | Required Role |
|----------|-------|---------------|
| View Notifications | `/notifications` | `notifications:read` |
| Create Notification | `/notifications` | `notifications:create` |
| Archive Notification | `/notifications` | `notifications:archive` |

---

## Roles Reference

| Role | Purpose |
|------|---------|
| `role:read` | View roles page |
| `role:create` | Create new roles |
| `role:delete` | Delete roles |
| `groups:read` | View groups |
| `groups:create` | Create groups |
| `accounts:read` | View accounts |
| `accounts:invite` | Generate invite codes |
| `accounts:archive` | Archive accounts |
| `estate:read` | View estate/buildings |
| `estate:manage` | Manage estate |
| `votings:readAll` | View all votings |
| `votings:read` | View finished votings |
| `votings:manage` | Create/edit/delete votings |
| `votings:participate` | Participate in voting |
| `notifications:read` | View notifications |
| `notifications:create` | Create notifications |
| `notifications:archive` | Archive notifications |