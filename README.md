# Subs Buddy
Your subtitle translating pal. Uploads an English .srt file and translates it to Bulgarian using Google's API. 

This application has been developed as a practical exercise for the CI/CD course lectured by Dave Farley. It follows the TDD, DDD and CI/CD principles. The main task for this is the Github actions pipeline which follows the CI/CD principles and recommendations as described by Dave.

Link to the course: https://courses.cd.training/collections

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