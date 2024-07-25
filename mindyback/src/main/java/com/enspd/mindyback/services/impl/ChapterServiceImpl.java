package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.exception.EntityNotFoundException;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.repository.ChapterRepository;
import com.enspd.mindyback.services.ChapterService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Override
    public ChapterDto createChapter(ChapterDto chapterDto) {
        return ChapterDto.fromEntity(chapterRepository.save(ChapterDto.toEntity(chapterDto)));
    }

    @Override
    public ChapterDto findChapterById(Integer id) {
        return ChapterDto.fromEntity(chapterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aucun chapitre trouvé avec l id :" + id, ErrorCodes.CHAPTER_NOT_FOUND)));

    }

    @Override
    public ChapterDto updateChapter(ChapterDto chapterDto) {
        return ChapterDto.fromEntity(chapterRepository.save(ChapterDto.toEntity(chapterDto)));
    }

    @Override
    public void deleteChapter(Integer id) {
        if (chapterRepository.existsById(id))
            chapterRepository.deleteById(id);
        else
            throw new EntityNotFoundException("Aucun chapitre trouvé avec l id :" + id, ErrorCodes.CHAPTER_NOT_FOUND);
    }

    @Override
    @Transactional
    public void updateChapterCurrent(Integer id) {
        List<Chapter> chapters = chapterRepository.findAllCurrentByCompetenceId(id);
        chapters.forEach(chapter -> chapter.setCurrent(false));
        Chapter chapter = chapterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Impossible de maj ce chapitre comme courrant avec l id :" + id, ErrorCodes.CHAPTER_CANNOT_BE_SET_CURRENT));
        chapter.setCurrent(true);
        chapterRepository.save(chapter);
    }

    @Override
    @Transactional
    public void validateChapter(Integer id) {
        Chapter chapter = chapterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Impossible de maj ce chapitre comme valider aucun trouvé avec l id :" + id, ErrorCodes.CHAPTER_CANNOT_BE_VALID));
        chapter.setCurrent(false);
        chapter.setCompleted(true);
        chapterRepository.save(chapter);
    }

    @Override
    public ChapterDto getChapter(Integer id) {
        return ChapterDto.fromEntity(chapterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aucun chapitre trouvé avec l id :" + id, ErrorCodes.CHAPTER_NOT_FOUND)));
    }

    @Override
    public List<ChapterDto> getAllChapters() {
        return chapterRepository.findAll().stream().map(ChapterDto::fromEntity).toList();
    }

    @Override
    @Transactional
    public List<ChapterDto> createAllChapters(List<ChapterDto> chaptersDto) {

        List<Chapter> chapters = chapterRepository.saveAll(
                chaptersDto.stream().map(ChapterDto::toEntity).toList()
        );
        chapters.get(0).setCurrent(true);
        return chapters.stream().map(ChapterDto::fromEntity).toList();
    }

    @Override
    public List<ChapterDto> getAllChapterByCompetence(Integer idCompetence) {
        List<Chapter> chapters = chapterRepository.findAllByCompetenceId(idCompetence).orElseThrow(() -> new EntityNotFoundException("Aucun chapitre trouvé pour cette competence avec l id :" + idCompetence, ErrorCodes.CHAPTER_NOT_FOUND));
        return chapters.stream().map(ChapterDto::fromEntity).toList();
    }
}
