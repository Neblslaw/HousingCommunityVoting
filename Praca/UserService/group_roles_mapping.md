# Group to Role Mappings

## Accounts Manager
**Roles**: role:create, groups:create, accounts:read, accounts:archive, groups:edit, groups:read, accounts:invite, role:delete, groups:delete, role:read

**Use Cases**:
- Role Administration: View All Roles, Create Role, Delete Role
- Group Management: View All Groups, Create Group, View Group Details, Manage Group Members, Delete Group
- Account Administration: View All Accounts, Generate Invite Code, View Account Details, Archive Account

## Voting Creator
**Roles**: estate:read, accounts:read, votings:read, votings:manage, votings:readAll

**Use Cases**:
- Estate Management: View Buildings
- Account Administration: View All Accounts, View Account Details
- Voting Management: View Votings (All tab), View Votings (Owned tab), View Votings (Finished tab), Create Voting, Edit Voting, View Voting Details, Delete Voting, Lock Voting, View Live Voting, Start/End Live Voting, View Results

## Estate Manager
**Roles**: estate:read, accounts:read, estate:manage

**Use Cases**:
- Estate Management: View Buildings, CRUD Buildings/Floors/Flats
- Account Administration: View All Accounts, View Account Details

## Voting participant
**Roles**: votings:participate, votings:read, votings:readAll

**Use Cases**:
- Voting Management: View Votings (All tab), View Votings (Available tab), View Votings (Finished tab), View Voting Details, View Live Voting, Cast Vote, View Results

## Notifications Manager
**Roles**: notifications:create, notifications:read, notifications:archive

**Use Cases**:
- Notifications: View Notifications, Create Notification, Archive Notification