let app = angular.module('DreamCompany', [])
    .controller('projectCtrl', function ($scope, $http) {

        $scope.formatProject = function (project) {
            return '<div class="project-row row d-flex justify-content-around">'
                + `<a href="/projects/details/${project.id}">`
                + '<div class="home-card mt-4">'
                + `<h3 class="title">${project.name}</h3>`
                + `<div class="bar">`
                + `<div class="emptybar"></div>`
                + `<div class="filledbar"></div>`
                + `</div>`
                + `<div class="home-circle">`
                + `<svg class="svg" version="1.1" xmlns="http://www.w3.org/2000/svg">`
                + `<circle class="stroke" cx="60" cy="60" r="50"/>`
                + `</svg>`
                + `</div>`
                + `</div>`
                + `</a>`
                + `</div>`
        };

        $scope.fetchProjects = function ($event) {
            let status = $event.currentTarget.value;
            $http.get('http://localhost:8000/projects/fetch/' + status)
                .then(function (response) {
                    let projectContainer = document.getElementById('columns');
                    let noDataContainer = document.getElementById('no-data');
                    projectContainer.innerHTML = '';
                    noDataContainer.innerHTML = '';

                    if (response.data.length === 0){
                        noDataContainer.innerText = 'No projects with such status.'
                    }else {
                       $scope.projects = response.data.forEach(d => projectContainer.innerHTML += $scope.formatProject(d));
                    }
                });
        };
    });