    <template>
        <div class="selected-page">
            <h3>{{ currentPageList.pageNo }}</h3>
            <div class="drag-image">
                <div class="object" ref="pageObject">
                
                </div>
            </div>
        </div>
    </template>
    <script>
    export default {
        props : {
            currentPageList : Object,
            imageIndex : Number,
        },
        data(){
            return {
            }
        },
        mounted() {
            const dragArea = document.querySelector('.selected-page');
            const objArea = document.querySelector('.selected-page .drag-image .object');
            let toolMenu = this;
            let active = false;
            let currentX;
            let currentY;
            let currentXOffset;
            let currentYOffset;
            let moveX;
            let moveY;
            let currentObjId = null;

            objArea.addEventListener("mousedown", dragStart, {capture:false});
            objArea.addEventListener("mouseup", dragEnd, {capture:false});
            objArea.addEventListener("mousemove", drag, {capture:false});
            
            function dragStart(e) {
                e.stopPropagation();
                e.preventDefault();
                currentObjId = e.target.dataset.objId;
                currentX = e.pageX - dragArea.offsetLeft;
                currentY = e.pageY - dragArea.offsetTop;
                currentXOffset = e.pageX - dragArea.offsetLeft - e.offsetX;
                currentYOffset = e.pageY - dragArea.offsetTop - e.offsetY;
                document.body.style.cursor = 'grabbing';
                e.target.style.opacity = '0.5';
                active = true;
            };

            function drag(e) {
                e.preventDefault();
                e.stopPropagation();
                if (active && currentObjId === e.target.dataset.objId) {
                    moveX = e.pageX - dragArea.offsetLeft;
                    moveY = e.pageY - dragArea.offsetTop;
                    setLocation(moveX - currentX, moveY - currentY, e.target);
                }else if(currentObjId !== e.target.dataset.objId){
                    e.preventDefault();
                    e.stopPropagation();
                }
            };

            function setLocation(xMove, yMove, element) {
                element.style.left = currentXOffset + xMove+ "px";
                element.style.top = currentYOffset + yMove + "px";
                element.style.zIndex = "10";
            };

            function dragEnd(e) {
                e.target.style.zIndex = "1";
                e.target.style.opacity = '1';
                let targetObj = e.target.dataset.objId;
                //객체에도 변한 위치를 적용시킴
                let result = toolMenu.currentPageList.imageList.find(el => el.objId === targetObj);
                result.style.left = e.target.style.left;
                result.style.top = e.target.style.top;
                document.body.style.cursor = '';
                active = false;
            };
        },
        watch: {
            currentPageList() {
                this.updateContent();
            },
        },
        methods: {
            updateContent() {
                const objectElement = this.$refs.pageObject;
                while (objectElement.firstChild) {
                    objectElement.removeChild(objectElement.firstChild);
                }
                if(this.currentPageList.imageList != null) {
                    for (const [index, image] of Object.entries(this.currentPageList.imageList)) {
                        const imageEle = document.createElement('img');
                        imageEle.src = image.src;
                        imageEle.id = image.id;
                        imageEle.dataset.objId = image.objId;
                        imageEle.style.left = image.style.left;
                        imageEle.style.top = image.style.top;
                        imageEle.style.position = image.style.position;
                        imageEle.style.width = image.style.width;
                        imageEle.style.height = image.style.height;
                        imageEle.setAttribute("draggable", "false");
                        imageEle.style.zIndex = 1;
                        objectElement.appendChild(imageEle);
                    }
                }
            },
        },
    }

</script>
<style scoped>
.selected-page{
    margin: 10px;
    width: 80%;
    height: 450px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    touch-action: none;
    position: relative;
    
}
.character-image{
    width: 150px;
    text-align: center;
}

.drag-image {
    margin: 10px;
    width: 100%;
    height: 450px;
    position: absolute;
    border: 1px solid gray;
}
.object {
    position: absolute;
}
</style>