const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase)
exports.pushNotification = functions.firestore.document("Food/{user_id}").onWrite(event => {	
const locn = event.after.data();
console.log(locn);
const payload = {
        notification: {
            title:"EyesOfHawk : Attention Hungry Hawks",
            body: "Food is available @ "+locn.desc+" "+locn.room_no,
            sound: "default"
        },
    };
  //Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };
    return admin.messaging().sendToTopic("pushNotifications", payload, options);
});
