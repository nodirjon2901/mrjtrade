package org.example.mrj.domain;

import org.example.mrj.domain.entity.Banner;

import java.io.Serializable;

public record BannerWrapper(Banner banner) implements Serializable
{
}
