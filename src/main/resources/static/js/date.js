
window.addEventListener("resize", findCurrentWidthHeight);

function displayDateTime() {
    const now = new Date();
    const options = {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: true
    };

    const dateTimeString = now.toLocaleString('en-GB', options).replace(',', ' -');

    const dateElement = document.querySelector('.date');
    dateElement.innerText = dateTimeString;
}

displayDateTime();
setInterval(displayDateTime, 1000);

function adjustAnimationDuration() {
    const runningText = document.querySelector('.announcement');
    const textLength = runningText.textContent.length;
    const animationDuration = textLength * 0.05 + 's';

    runningText.style.animationDuration = animationDuration;
}

adjustAnimationDuration();
window.addEventListener('resize', adjustAnimationDuration);

function findCurrentWidthHeight() {
    const width = window.innerWidth;
    const height = window.innerHeight;

    document.getElementById("dimensions").innerHTML = `Width: ${width}, Height: ${height}`;
}
