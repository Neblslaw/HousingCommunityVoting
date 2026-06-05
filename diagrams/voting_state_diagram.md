## Voting States

The state is determined by three timestamp fields in the `Voting` entity:
- `lockTime` - when voting is locked (prepared to start)
- `startTime` - when voting begins
- `endTime` - when voting finishes

### State Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                     │
│  ┌─────────────┐    lock()     ┌─────────┐    startVoting()      │
│  │  CREATED    │ ───────────▶ │ LOCKED  │ ───────────────────┐   │
│  │(unlocked)   │               │         │                    │   │
│  └─────────────┘               └─────────┘                    │   │
│        │                         ▲                              │   │
│        │                         │                              │   │
│   createVoting()           startVoting()                       │
│        │                         │                              ▼   │
│        │                  ┌──────────────┐   endVoting()   ┌───────┤
│        ▼                  │  IN_PROGRESS │ ──────────────▶ │ ENDED │
│   deleteVoting()         │ (started)    │                 │       │
│   updateVoting()         └──────────────┘                 └───────┘
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

## State Details

| State | Conditions | Allowed Operations |
|-------|-------------|---------------------|
| **CREATED** | `lockTime = null` | create, update, delete, lock |
| **LOCKED** | `lockTime != null`, `startTime = null` | startVoting() only |
| **IN_PROGRESS** | `startTime != null`, `endTime = null` | vote() (via WebSocket) |
| **ENDED** | `endTime != null` | view results only |

## What Can Be Done in Each State

### 1. CREATED (`VotingWriteService.java:43-82`)
- `createVoting()` - create new voting
- `updateVoting()` - modify name, questions, groups (if not locked)
- `deleteVoting()` - remove voting (if not started)

### 2. LOCKED (`VotingWriteService.java:191-200`)
- `lockVoting()` - lock the voting to prepare for start
- `startVoting()` - begin the voting (creates VoteAbility records for all voters)

### 3. IN_PROGRESS (`VotingWriteService.java:115-134`)
- `vote()` - cast a vote via WebSocket (only if VoteAbility exists for user/question)

### 4. ENDED (`VotingWriteService.java:139-186`)
- `endVoting()` - close voting, calculate results, delete all votes
- `getResults()` - view final results

## State Transition Rules (from VotingWriteService)

- **updateVoting**: fails if `lockTime != null` (line 52-53)
- **deleteVoting**: fails if `lockTime != null` OR `startTime != null` (lines 75-80)
- **startVoting**: fails if `lockTime == null` OR `startTime != null` (lines 89-94)
- **vote**: allowed only if user has VoteAbility for the question
- **endVoting**: fails if `startTime == null` OR `endTime != null` (lines 144-149)