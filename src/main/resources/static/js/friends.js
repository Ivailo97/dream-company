$('#send').click(function () {

    let receiverId = $('#friendId').val();
    let senderUsername = $('#senderUsername').val();

    alert('Request send');

    fetch('/api/requests/send', {
        method: 'post',
        body: JSON.stringify({
            senderUsername: senderUsername,
            receiverId: receiverId
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(data => {
            console.log(data);
            window.location = '/home';
        });

    return false;
});

$('#remove-friend').click(function () {

    let friendUsername= $('#friendUsername').val();

    alert('Friend removed');

    fetch('/users/remove-friend', {
        method: 'post',
        body: friendUsername,
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(data => {
            console.log(data);
            window.location = '/home';
        });

    return false;
});