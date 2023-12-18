<script setup>
import DownloadIcon from './icons/DownloadIcon.vue';
import FileIcon from './icons/FileIcon.vue';
import GifIcon from './icons/GifIcon.vue';
import ImageIcon from './icons/ImageIcon.vue';
import PDFIcon2 from './icons/PDFIcon2.vue';
import ZipIcon from './icons/ZipIcon.vue';
import DocxIcon from './icons/DocxIcon.vue';
import { defineProps } from 'vue'
import OpenFile from './icons/OpenFile.vue';
const props = defineProps({
    file: {
        type: Object,
        required: true
    }
})
const downloadFile = async (unqFileName) => {
    if (unqFileName) {
        const FileUrl = import.meta.env.VITE_ROOT_API + `/api/file/${unqFileName}`
        window.open(FileUrl, '_blank')
    }
}
const getType = (originalName) => {
    const ext = originalName.split('.').pop()
    return ext
}
</script>
<template>
    <div>
        <button @click="downloadFile(file?.uniqueFileName)"
            class="flex justify-between rounded-lg bg-zinc-100 mb-2 hover:bg-zinc-200 transition-colors">
            <div class="my-auto mx-3">
                <ZipIcon v-if="getType(file?.originalFileName) === 'zip' || getType(file?.originalFileName) === 'rar'" />
                <GifIcon v-else-if="getType(file?.originalFileName) === 'Gif'" />
                <PDFIcon2 v-else-if="getType(file?.originalFileName) === 'pdf'" />
                <DocxIcon
                    v-else-if="getType(file?.originalFileName) === 'docx' || getType(file?.originalFileName) === 'doc'" />
                <ImageIcon v-else-if="getType(file?.originalFileName) === 'png' || getType(file?.originalFileName) === 'jpg'
                    || getType(file?.originalFileName) === 'jpeg'" />
                <FileIcon v-else />
            </div>
            <div class=" py-2">{{ file?.originalFileName }}
            </div>
            <DownloadIcon class="my-auto mx-3"
                v-if="!['png', 'jpg', 'jpeg', 'pdf'].includes(getType(file.originalFileName))" />
            <OpenFile class="my-auto mx-3" v-else />

        </button>
    </div>
</template>
 
<style scoped></style>