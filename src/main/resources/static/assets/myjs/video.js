const videoForm = document.getElementById('videoForm');
const eCheckBoxPlaylists = document.getElementsByName('playlists');
const tBody = document.getElementById("tBody");
const ePagination = document.getElementById('pagination')
const eSearch = document.getElementById('search');
const formBody = document.getElementById('formBody');

let videoSelected = {};
let pageable = {
    page: 1,
    sort: 'id,desc',
    search: ''
}

let playlists;
let videos = [];

videoForm.onsubmit = async (e) => {
    e.preventDefault();
    let data = getDataFromForm(videoForm);
    data = {
        ...data,
        idPlaylists: Array.from(eCheckBoxPlaylists)
            .filter(e => e.checked)
            .map(e => e.value),
        id: videoSelected.id
    }

    if (videoSelected.id) {
        await editVideo(data);
        webToast.Success({
            status: 'Sửa thành công',
            message: '',
            delay: 2000,
            align: 'topright'
        });
    } else {
        await createVideo(data)
        webToast.Success({
            status: 'Thêm thành công',
            message: '',
            delay: 2000,
            align: 'topright'
        });
    }
    await renderTable();
    $('#staticBackdrop').modal('hide');

}
function getDataFromForm(form) {
    const data = new FormData(form);
    return Object.fromEntries(data.entries())
}
async function getPlaylistsSelectOption() {
    const res = await fetch('api/playlists');
    return await res.json();
}
async function getList() {
    const response = await fetch(`/api/videos?page=${pageable.page - 1 || 0}&search=${pageable.search || ''}`);

    if (!response.ok) {
        throw new Error("Failed to fetch data");
    }

    const result = await response.json();
    pageable = {
        ...pageable,
        ...result
    };
    genderPagination();
    renderTBody(result.content);
    return result;
}
function renderTBody(items) {
    let str = '';
    items.forEach(e => {
        str += renderItemStr(e);
    })
    tBody.innerHTML = str;
}
const genderPagination = () => {
    ePagination.innerHTML = '';
    let str = '';

    str += `<li class="page-item ${pageable.first ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
            </li>`

    for (let i = 1; i <= pageable.totalPages; i++) {
        str += ` <li class="page-item ${(pageable.page) === i ? 'active' : ''}" aria-current="page">
      <a class="page-link" href="#">${i}</a>
    </li>`
    }

    str += `<li class="page-item ${pageable.last ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Next</a>
            </li>`
    ePagination.innerHTML = str;

    const ePages = ePagination.querySelectorAll('li');
    const ePrevious = ePages[0];
    const eNext = ePages[ePages.length-1]

    ePrevious.onclick = () => {
        if(pageable.page === 1){
            return;
        }
        pageable.page -= 1;
        getList();
    }
    eNext.onclick = () => {
        if(pageable.page === pageable.totalPages){
            return;
        }
        pageable.page += 1;
        getList();
    }
    for (let i = 1; i < ePages.length - 1; i++) {
        if(i === pageable.page){
            continue;
        }
        ePages[i].onclick = () => {
            pageable.page = i;
            getList();
        }
    }
}
const onSearch = (e) => {
    e.preventDefault()
    pageable.search = eSearch.value;
    pageable.page = 1;
    getList();
}
const searchInput = document.querySelector('#search');
searchInput.addEventListener('search', () => {
    onSearch(event)
});
function renderItemStr(item) {
    return `<tr>                
                    <td title="${item.description}">
                        ${item.title}
                    </td>
                    <td>
                        ${item.description}
                    </td>
                    <td>
                        ${item.videoPlaylist}
                    </td>
                    <td>
                         <div class="dropdown">
                             <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                             <i class="bx bx-dots-vertical-rounded"></i>
                            </button>
                        <div class="dropdown-menu">
                        <button class="dropdown-item" onclick="showEdit(${item.id})"
                        data-bs-toggle="modal" data-bs-target="#staticBackdrop"
                        ><i class="bx bx-edit-alt me-1"></i> Edit</button
                            >
                        <button class="dropdown-item" onclick="deleteVideo(${item.id})"
                        ><i class="bx bx-trash me-1"></i> Delete</button
                        >
              </div>
            </div>
                    </td>
                </tr>`
}
window.onload = async () => {
    playlists = await getPlaylistsSelectOption();
    await renderTable()
    renderForm(formBody, getDataInput());
}
function getDataInput() {
    return [
        {
            label: 'Title',
            name: 'title',
            value: videoSelected.title,
            required: true,
            pattern: "^[A-Za-z ]{8,30}$",
            message: "Title must have minimum is 8 characters and maximum is 30 characters",
        },
        {
            label: 'Description',
            name: 'description',
            value: videoSelected.description,
            pattern: "^[A-Za-z ]{6,120}",
            message: "Description must have minimum is 6 characters and maximum is 120 characters",
            required: true
        },
        {
            label: 'Url',
            name: 'url',
            value: videoSelected.url,
            required: true,
            pattern: "^(https?|ftp):\\/\\/[A-Za-z0-9\\.-]+\\.[A-Za-z]{2,6}(\\S*)$",
            message: "Please enter a valid URL.",
        },
    ];
}
async function renderTable() {
    const pageable = await getList();
    videos = pageable.content;
    renderTBody(videos);
}
const findById = async (id) => {
    const response = await fetch('/api/videos/' + id);
    return await response.json();
}
function showCreate() {
    $('#staticBackdropLabel').text('Create Video');
    clearForm();
    renderForm(formBody, getDataInput())
}
async function showEdit(id) {
    $('#staticBackdropLabel').text('Edit Video');
    clearForm();
    videoSelected = await findById(id);
    videoSelected.playlistIds.forEach(idPlaylist => {
        for (let i = 0; i < eCheckBoxPlaylists.length; i++) {
            if (idPlaylist === +eCheckBoxPlaylists[i].value) {
                eCheckBoxPlaylists[i].checked = true;
            }
        }
    })
    renderForm(formBody, getDataInput());
}
function clearForm() {
    videoForm.reset();
    videoSelected = {};
}
async function editVideo(data) {
    const res = await fetch('/api/videos/' + data.id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
}
async function deleteVideo(id) {
    const confirmBox = webToast.confirm("Are you sure to delete Video " + id + "?");
    confirmBox.click(async function () {
        const res = await fetch('/api/videos/' + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(id)
        });
        if (res.ok) {
            webToast.Success({
                status: 'Xóa thành công',
                message: '',
                delay: 2000,
                align: 'topright'
            });
            await getList();
        } else {
            webToast.Danger({
                status: 'Xóa thất bại',
                message: '',
                delay: 2000,
                align: 'topright'
            });
        }
    });
}
async function createVideo(data) {
    const res = await fetch('/api/videos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
}

