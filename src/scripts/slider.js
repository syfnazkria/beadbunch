window.onload = function () {
    let currentIndex = 0; // Starting slide index
    const slides = document.querySelectorAll(".slide");
    const totalSlides = slides.length;
    const sliderContainer = document.querySelector(".slider-container");

    // Function to move the slider container
    function changeSlide() {
        currentIndex++;
        if (currentIndex >= totalSlides) {
            currentIndex = 0; // Reset to the first slide
        }

        // Move the slider container to show the current slide
        sliderContainer.style.transform = `translateX(-${currentIndex * 100}vw)`; // Move by 100vw (viewport width)
    }

    // Set interval for automatic slide change
    setInterval(changeSlide, 3000); // Change every 3 seconds
};
