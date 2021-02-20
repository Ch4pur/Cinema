document.addEventListener( 'DOMContentLoaded', function () {
    new Splide( '#slider', {
        type        : 'loop',
        perPage     : 4,
        perMove     : 1,
        gap         : 1,
        rewind      : true,
        pagination  : false,
    } ).mount();
} );