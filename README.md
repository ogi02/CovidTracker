# CovidTracker

Mobile application, which helps people know if they are COVID-19 infected before they show symptoms. Every minute every device makes a Bluetooth request to every other device nearby. Via ML we know which mode of transportation every user uses - car, still, walking, bus or train. If a user marks themselves as COVID-19 infected, we send notifications to every other user, who may have been have had contact with them. Every notification contains a map, which displays the exact point, where the user has been infected as well as is what circumstance have they been infected.

<div style="display: flex">
  <img src="./screenshots/main_activity.jpg" alt="main activity">
  <img src="./screenshots/map_activity.jpg" alt="map activity">
  <img src="./screenshots/notification.jpg" alt="notification">
</div>

## Installation

### Requirements

- Java 8
- Android SDK 30
- Gradle
- Android Studio or equivalent
- Node 14.x.x
- NPM / Yarn
- Python 3.8
- Poetry

### Running

- To run the mobile app create configuration for Android app, choose MainActivity for default one, build it and run it
- To run the server create `.env` file with the fields from `.env.example`, run `yarn install` and `yarn start`
- To run the server determining the transportation mode ....

## Authors

- **Samuil Georgiev** - Android
- **Valentin Spasov** - Android
- **Anton Yanchev** - Backend
- **Ognian Baruh** - Backend
- **Victor Gorchilov** - Machine Learning
