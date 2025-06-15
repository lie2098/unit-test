  function showOrHideGrade(gradeType) {
        if (gradeType === "math") {
              var x = document.getElementById("mathGrade");
              if (x.style.display === "none") {
                x.style.display = "block";
              } else {
                x.style.display = "none";
              }
        }
        if (gradeType === "science") {
             var x = document.getElementById("scienceGrade");
             if (x.style.display === "none") {
                 x.style.display = "block";
            } else {
                x.style.display = "none";
            }
          }
        if (gradeType === "history") {
             var x = document.getElementById("historyGrade");
             if (x.style.display === "none") {
                 x.style.display = "block";
            } else {
                x.style.display = "none";
            }
          }
    }

    function deleteStudent(id) {
    window.location.href = "/delete/student/" + id;
    }

    function deleteMathGrade(studentId, id) {
    window.location.href = "/student/" + studentId + "/grades/" + id + "/" + "math";
    }

    function deleteScienceGrade(studentId, id) {
    window.location.href = "/student/" + studentId + "/grades/" + id + "/" + "science";
    }

    function deleteHistoryGrade(studentId, id) {
    window.location.href = "/student/" + studentId + "/grades/" + id + "/" + "history";
    }

    function studentInfo(id) {
        window.location.href = "/studentInformation/" + id;
    }