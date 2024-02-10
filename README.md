# Subs Buddy
Your subtitle translating pal


## Architecture

```
 ┌────────────────┐
 │ MVC            │
 └────────────────┘
         ▲
         │
         ▼
 ┌────────────────┐
 │ Domain(CQRS)   │
 └────────────────┘
         ▲
         │
         ▼
 ┌────────────────┐
 │ Database       │
 └────────────────┘
```