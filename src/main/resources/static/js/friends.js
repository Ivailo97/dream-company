$('#accept').click(function () {

    let requestId = $('#accept-id').val();

    alert('Request accepted');

    fetch('/api/requests/accept', {
        method: 'post',
        body: requestId,
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


$('#send').click(function () {

    let receiverId = $('#friendId').val();
    let senderUsername = $('#senderUsername').val();

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
            $('#myModal').modal('toggle');
            $('#send').hide();
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

$('#accept-request').click(function () {

    let requestId = $('#requestId').val();

    console.log(requestId);

    alert('Request accepted');

    fetch('/api/requests/accept', {
        method: 'post',
        body: requestId,
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